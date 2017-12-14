(ns omegle.views.page-login
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [omegle.views :refer [page-layout]]))

(defn login-form
  []
  [:div
    [:form {:class "mui-form"}
      [:div {:class "mui-textfield"}
        [:input {:type "text" :placeholder "Username"}]]
      [:button {:class "mui-btn mui-btn--raised" :type "submit"}
        "Submit"]
      ]])

(defn main-content
  []
    [:div
      [:h2 "Login"]
      (login-form)
      [:div [:a {:href "/"} "go to main page"]]
    ])

(defn render
  []
  (page-layout main-content))
