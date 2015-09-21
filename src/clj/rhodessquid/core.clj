(ns rhodessquid.core
  (:require [liberator.core :refer [defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer :all]
            [clojure.tools.logging :as log]
            [compojure.route :as route]
            [rhodessquid.db :as db]
            [clojure.java.jdbc :as jdbc]))

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

(defresource translation [key lang]
  :allowed-methods [:get :post :put]
  :available-media-types ["text/plain"]
  :exists? translation-for
  :handle-ok (fn [{phrase :phrase}] phrase)
  :put! write-translation!
  :post! write-translation!
  :handle-not-found (str "Translation for key: '" key "', lang: '" lang "' not found"))

(defresource translations []
  :allowed-methods [:get]
  :available-media-types ["text/plain"]
  :handle-ok list-phrases)

(defroutes app
  (ANY "/phrases/:key/:lang" [key lang]  (translation key lang))
  (ANY "/phrases" [] (translations)))

(def handler
  (-> app wrap-params))
