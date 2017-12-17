(ns omegle.views
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]))

(defn logged-in
  []
  @(subscribe [:username]))

(defn appbar
  []
  [:div {:class
      (str "mui-appbar " (if logged-in
        "animate--fadeDown" ""))}
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
            [:td {:class "mui--text-title"}
              [:span
                (str (if @(subscribe [:update-tokens])
                          @(subscribe [:update-tokens])
                          0) " " "tokens")]]

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
