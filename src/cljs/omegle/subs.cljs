(ns omegle.subs
  (:require
    [re-frame.core :refer [reg-sub subscribe]]))

(defn update-username
  [db _]
  (-> db :user :username))
(reg-sub :username update-username)

(defn update-video-preview-source
  [db _]
  (-> db :video-preview :src))
(reg-sub :update-video-preview-source update-video-preview-source)

(defn update-tokens
  [db _]
  (-> db :tokens))
(reg-sub :update-tokens update-tokens)

(defn form-chat-message
  [db _]
  (-> db :forms :chat :message))
(reg-sub :form-chat-message form-chat-message)

(defn chat-messages
  [db _]
  (-> db :chat :messages))
(reg-sub :chat-messages chat-messages)
