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

(defn video-player-source
  [db _]
  (-> db :video-player-source))
(reg-sub :video-player-source video-player-source)

(defn token-counter-started
  [db _]
  (-> db :token-counter-started))
(reg-sub :token-counter-started token-counter-started)

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
