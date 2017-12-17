(ns omegle.views
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]))

(defn logged-in
  []
  @(subscribe [:username]))

(defn appbar
  []
  [:div {:class
      (str "appbar " (if logged-in
        "animate--fadeDown" ""))}
    [:div {:class "container"}
      [:div {:class "appbar--bar"}
        [:nav {:class "appbar--menu"}
          [:ul
            [:li
              [:a {:class "appbar--link" :href "/"}
                "Chat"]]
            ]]
        [:div {:class "appbar--menu--secondary"}
          [:span {:class "appbar--item"}
            (str (if @(subscribe [:update-tokens])
                      @(subscribe [:update-tokens])
                      0) " " "tokens")]
          [:a {:class "appbar--link" :href "/profile"}
            (let [username @(subscribe [:username])]
              (if username username "Profile"))]
        ]
      ]
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
