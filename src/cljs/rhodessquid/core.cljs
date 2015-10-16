(ns rhodessquid.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [ajax.core :refer [GET]]
            [cljs.reader :refer [read-string]])
  (:import goog.History))

;; -------------------------
;; State

(defonce state (reagent/atom {:phrases []}))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to rhodessquid"]
   [:div [:a {:href "#/about"} "goto"]]
   [:ul
    (doseq [phrase (:phrases @state)]
      [:li (:phrase phrase) ]
      [:p "I'm here"])]])

(defn about-page []
  [:div [:h2 "About rhodessquid"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])
nnn
(defn log-me! [response]
  (.log js/console (str response)))

(defn update-state [response]
  (swap! state assoc :phrases response))


(defn log-state []
  (log-me! @state))

(defn get-all []
  (GET "/phrases" {:handler update-state
                   :response-format :edn}))
;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
