(ns omegle.views.page-main
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [omegle.views :refer [page-layout]]))

(defn start-preview
  [params]
  (dispatch [:video-preview-source (.createObjectURL js/window.URL params)]))

(defn load-webcam
  []
  (.then
    (.getUserMedia
      (-> js/window .-navigator .-mediaDevices)
      #js{:audio true :video true})
    #(start-preview %)))

(defn camera-preview
  []
  [:div
    [:p "Camera"]
    [:video {:src @(subscribe [:update-video-preview-source])}]
  ])

(defn video-preview
  []
  (load-webcam)
  [:div
    [:p "Preview"]
    [:video]
  ])

(defn camera-view
  []
  [:div
    [video-preview]
    [camera-preview]
  ])

(defn main-content
  []
    [:div
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
