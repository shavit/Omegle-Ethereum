(ns omegle.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx
              path trim-v after debug]]
            [secretary.core :as secretary :include-macros true]
            [omegle.db :refer [default-db omegle->local-store]]
            [cljs.spec.alpha :as spec]))

(reg-event-db
  :change-form-username
  (fn [db [_ v]]
    (assoc-in db [:forms :username] v)))

(reg-event-db
  :update-form-username
  (fn [db [_ v]]
    ; (println "Dispatch route")
    ; (secretary/dispatch! "/")
    (assoc-in db [:user :username]
      (-> db :forms :username))))

(reg-event-db
  :video-preview-source
  (fn [db [_ url]]
    (assoc-in db [:video-preview :src]
      url)))

(reg-event-db
  :load-tokens
  (fn [db _]
    (assoc db :tokens 1000)))

(reg-event-db
  :update-tokens
  (fn [db [_ v]]
    (assoc db :tokens v)))
