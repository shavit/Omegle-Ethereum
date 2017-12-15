(ns omegle.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [goog.events :as events]
              [omegle.events]
              [omegle.subs]
              [omegle.views.page-main :as page-main]
              [omegle.views.page-login :as page-login])
    (:import  [goog History]
              [goog.history EventType]))

;; -------------------------
;; Routes

(def page (atom #(page-main/render)))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #(page-main/render)))

(secretary/defroute "/login" []
  (reset! page #(page-login/render)))

(def history
  (doto (History.)
    (events/listen EventType.NAVIGATE
      (fn [event]
        (secretary/dispatch!
          (.-token event))))
    (.setEnabled true)))

; TODO: Subscribe to route change

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
