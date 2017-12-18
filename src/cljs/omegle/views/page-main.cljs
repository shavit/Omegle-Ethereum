(ns omegle.views.page-main
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [omegle.views :refer [page-layout]]))

(defn start-webcam-preview
  []
  (dispatch [:start-webcam-preview])
  (.setInterval js/window
    (fn []
      (dispatch [:update-tokens -1]))
    1000))

(defn on-submit-message
  [event]
  (.preventDefault event)
  (dispatch [:update-form-chat-message])
  (dispatch [:change-form-chat-message nil]))

(defn chat-message
  [message]
  [:div {:key (:id message)
    :class "chat--message"}
    [:strong (str (:username message) ": ")]
    [:span (:body message)]
  ])

(defn chat-box
  []

  [:div {:class "control--chat"}
    [:div {:class "control--chat--messages"}
      (let [messages (reverse @(subscribe [:chat-messages]))
          n (count messages)]
        (for [m messages]
          (chat-message m)))
      ]
    [:form {:class "form--chat" :on-submit on-submit-message}
      [:input {:class "form--chat--input"
        :type "text"
        :autoFocus true
        :value @(subscribe [:form-chat-message])
        :on-change #(dispatch
          [:change-form-chat-message (-> % .-target .-value)])}]
    ]
    ])

(defn camera-preview
  []
  [:div {:class "video--box webcam--preview"}
    [:video {:autoPlay true
      :src @(subscribe [:update-video-preview-source])}]
  ])

(defn video-preview
  []
  (start-webcam-preview)
  [:div {:class "video--box video--player"}
    [:video]
  ])

(defn camera-view
  []
  [:div {:class "video--players"}
    [video-preview]
    [:div {:class "panel--chat"}
      [camera-preview]
      [chat-box]
    ]
  ])

(defn main-content
  []
    [:div {:class "container"}
      [:h2 (let [username @(subscribe [:username])]
        (str "Hello " (if username username "Guest")))]
      [:div
        (let [username @(subscribe [:username])]
          (if username
            nil
            [:a {:href "/profile"} "login"]))
      ]
      (let [username @(subscribe [:username])]
        (if username
          [camera-view]
          nil))
    ])

(defn render
  []
  (page-layout main-content))
