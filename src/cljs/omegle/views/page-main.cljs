(ns omegle.views.page-main
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [omegle.views :refer [page-layout]]))

(defn main-content
  []
    [:div
      [:h2 "Index page"]
      [:div [:a {:href "/login"} "go to login page"]]
    ])

(defn render
  []
  (page-layout main-content))
