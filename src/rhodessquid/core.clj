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
  ([ctx]
   (let [key (get-in ctx [:request :params :key])
         lang (get-in ctx [:request :params :lang])]
     (translation-for key lang)))
  ([key lang]
   (-> (db/find-translation db-spec key lang) first :phrase)))

(defn create-translation!
  ([ctx]
   (let [key (get-in ctx [:request :params :key])
         lang (get-in ctx [:request :params :lang])
         body(slurp (get-in ctx [:request :body]))]
     (create-translation! key lang body)))
  ([key lang phrase]
   (jdbc/with-db-transaction [connection db-spec]
     (let [key
           (:key_name (db/create-key<! connection key ))]
       (db/create-phrase! connection key lang phrase)))))

(defn update-translation!
  ([ctx]
   (let [key (get-in ctx [:request :params :key])
         lang (get-in ctx [:request :params :lang])
         body(slurp (get-in ctx [:request :body]))]
     (update-translation! key lang body)))
  ([key lang phrase]
   (db/update-phrase! db-spec phrase key lang)))

(defresource translations [key lang]
  :allowed-methods [:get :post]
  :available-media-types ["text/plain"]
  :exists? translation-for
  :handle-ok translation-for
  :post! update-translation!
  :handle-not-found (str "Translation for key: '" key "', lang: '" lang "' not found"))

(defroutes app
  (ANY "/phrases/:key/:lang" [key lang]  (translations key lang)))

(def handler
  (-> app wrap-params))
