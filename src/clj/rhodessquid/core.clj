(ns rhodessquid.core
  (:require [liberator.core :refer [defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [ANY GET defroutes]]
            [clojure.tools.logging :as log]
            [compojure.route :as route]
            [rhodessquid.db :as db]
            [clojure.java.jdbc :as jdbc]
            [hiccup.core :refer :all]
            [hiccup.page :refer [include-js include-css]]
            [environ.core :refer [env]]
            [prone.middleware :refer [wrap-exceptions]]))

(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//localhost:5432/rhodessquid"
              :user "mahnve"})

(defn translation-for
  ([{{{key :key lang :lang} :params} :request}]
   (translation-for key lang))
  ([key lang]
   {:phrase  (-> (db/find-phrase db-spec key lang) first :phrase)}))

(defn create-translation!
  ([key lang phrase]
   (jdbc/with-db-transaction [connection db-spec]
     (db/create-key<! connection key )
     (db/create-phrase! connection key lang phrase))))

(defn update-translation!
  ([key lang phrase]
   (db/update-phrase! db-spec phrase key lang)))

(defn write-translation!
  [{{{key :key lang :lang} :params body :body } :request phrase :phrase}]
  (let [body-data (slurp body)]
    (if phrase
      (update-translation! key lang body-data)
      (create-translation! key lang body-data))))

(defn list-phrases
  ([_] (list-phrases))
  ([] (pr-str (db/all-phrases db-spec))))


(defn client-page []
  (html [:html [:body [:div {:id "app"}]]
         (include-js "js/app.js")]))

(defresource translation [key lang]
  :allowed-methods [:get :post :put]
  :available-media-types ["application/edn"]
  :exists? translation-for
  :handle-ok (fn [{phrase :phrase}] phrase)
  :put! write-translation!
  :post! write-translation!
  :handle-not-found (str "Translation for key: '" key "', lang: '" lang "' not found"))

(defresource translations []
  :allowed-methods [:get]
  :available-media-types ["text/plain"]
  :handle-ok list-phrases)

(defroutes routes
  (ANY "/phrases/:key/:lang" [key lang]  (translation key lang))
  (ANY "/phrases" [] (translations))
  (GET "/" [] (client-page)))


(def app
  (let [handler (wrap-defaults #'routes site-defaults)]
    (if (env :dev) (-> handler wrap-exceptions wrap-reload) handler)))
