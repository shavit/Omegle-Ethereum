(ns omegle.views.page-main
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [omegle.views :refer [page-layout]]))


(defn start-clock
  []

  (if (= @(subscribe [:token-counter-started false]) false)
    (let [x
        (.setInterval js/window
          (fn []
            (dispatch [:update-tokens -1]))
          1000)]
      (dispatch [:set-token-counter x])))
  )

(defn check-balance
  []

  (if (<= @(subscribe [:update-tokens]) 0)
    (do
      (dispatch [:stop-token-counter])
      ))
  )

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

  [:div {:class "video--box video--player"}
    [:video {:autoPlay true
      :loop true
      :src @(subscribe [:video-player-source])}]
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

; (dispatch-sync [:eth/load-account])
(dispatch [:start-webcam-preview])
(dispatch [:video-player-source "https://openload.co/stream/ODREs1qrNVg~1513999061~46.116.0.0~r0Yoj_f2?mime=true"])
(defn main-content
  []
    (start-clock)
    (check-balance)

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
