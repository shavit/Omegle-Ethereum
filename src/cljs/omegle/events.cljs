(ns omegle.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx
              path trim-v after debug]]
            [omegle.db :refer [default-db omegle->local-store]]
            [cljs.spec.alpha :as spec]))

(reg-event-db
  :change-form-username
  (fn [db [_ v]]
    (assoc-in db [:forms :username] v)))

(reg-event-db
  :update-form-username
  (fn [db [_ v]]
    (assoc-in db [:user :username]
      (-> db :forms :username))))
