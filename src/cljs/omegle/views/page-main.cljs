(ns omegle.views.page-main
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [omegle.views :refer [page-layout]]))

(defn camera-preview
  []
  [:div
    [:p "Camera"]
    [:video]
  ])

(defn video-preview
  []
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
            [:a {:href "/login"} "login"]))
      ]
      (let [username @(subscribe [:username])]
        (if username
          [camera-view]
          nil))
    ])

(defn render
  []
  (page-layout main-content))
