(ns omegle.subs
  (:require
    [re-frame.core :refer [reg-sub subscribe]]))

(defn update-username
  [db _]
  (-> db :user :username))
(reg-sub :username update-username)
