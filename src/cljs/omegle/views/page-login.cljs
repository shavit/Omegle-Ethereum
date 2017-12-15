(ns omegle.views.page-login
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [omegle.views :refer [page-layout]]))

(defn on-submit-form
  [event]
  (.preventDefault event)
  (dispatch [:update-form-username]))

(defn login-form
  []
  [:div
    [:form {:class "mui-form"
            :on-submit on-submit-form}
      [:div {:class "mui-textfield"}
        [:input {:type "text"
                :placeholder "Username"
                :on-change #(dispatch
                  [:change-form-username (-> % .-target .-value)])}]]
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
