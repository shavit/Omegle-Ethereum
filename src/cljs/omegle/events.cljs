(ns omegle.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx
              path trim-v after debug dispatch]]
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

(defn start-webcam-preview
  [stream]
  (dispatch [:video-preview-source (.createObjectURL js/window.URL stream)])
  )

(reg-event-fx
  :start-webcam-preview
  (fn [_ _]
    (.then
      (.getUserMedia
        (-> js/window .-navigator .-mediaDevices)
        #js{:audio false :video true})
      #(start-webcam-preview %))
    nil))

(reg-event-db
  :update-tokens
  (fn [db [_ v]]
    (let [tokens (:tokens db)]
      (assoc db :tokens (+ tokens v)))))

(reg-event-db
  :change-form-chat-message
  (fn [db [_ v]]
    (assoc-in db [:forms :chat :message] v)))

(defn create-message
  [db]
  (let [messages (-> db :chat :messages)]
    {:id (if messages (count messages) 0)
      :username (-> db :user :username)
      :body (-> db :forms :chat :message)}
      ))

(reg-event-db
  :update-form-chat-message
  (fn [db _]
    (let [messages
        (concat (-> db :chat :messages)
        [(create-message db)])]
      (assoc-in db [:chat :messages] messages))))
