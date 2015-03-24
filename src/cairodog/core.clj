(ns cairodog.core
  (:require [liberator.core :refer [defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(def stored-words
  (atom { "cat" {"en" "Cat" "sv" "Katt"}}))

(defn return-word [ctx]
  (get-in ctx [:word]))

(defn word-for [ctx]
  (let [key (get-in ctx [:request :params :key])
        lang (get-in ctx [:request :params :lang])]
    (if-let [word (get-in @stored-words [key lang])]
      {:word word})))

(defn update-word [key]
  "hello")

(defresource words [key lang]
  :available-media-types ["text/plain"]
  :exists? word-for
  :allowed-methods [:get :post :put]
  :handle-ok return-word
  :post! update-word
  :handle-not-found "word for key/lang not found")

(defroutes app
  (GET "/words/:key/:lang" [key lang]  (words key lang)))

(def handler
  (-> app wrap-params))
