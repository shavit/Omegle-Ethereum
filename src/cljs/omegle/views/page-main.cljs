(ns omegle.views.page-main
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [omegle.views :refer [page-layout]]))

(defn main-content
  []
    [:div
      [:h2 (let [username @(subscribe [:username])]
        (str "Hello " (if username username "Guest")))]
      [:div [:a {:href "/login"} "go to login page"]]
    ])

(defn render
  []
  (page-layout main-content))
