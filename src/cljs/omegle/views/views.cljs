(ns omegle.views
  (:require [reagent.core :as reagent]))

(defn appbar
  []
  [:div {:class "mui-appbar"}
    [:div {:class "mui-container"}
      [:table
        [:tbody
          [:tr
            [:td {:class "mui--text-title"}
              [:a {:class "mui-btn" :href "/"}
                "Demo"]]
            [:td {:class "mui--text-title"}
              [:a {:class "mui-btn" :href "/profile"}
                "Profile"]]

                ]]]
      ]])

(defn page-layout
  [dom]
  [:div {:class "page"}
    [omegle.views/appbar]
    [:div {:id "main-content"}
      [:div {:class "mui-container"}
        [dom]
    ]]
  ])
