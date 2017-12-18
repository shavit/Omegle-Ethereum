(ns omegle.views.page-profile
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [secretary.core :as secretary :include-macros true]
            [omegle.views :refer [page-layout]]))

(defn on-submit-form
  [event]
  (.preventDefault event)
  (dispatch [:update-form-username])
  (dispatch [:update-tokens 1000])
  (.pushState js/window.history "" "" "/")
  (secretary/dispatch! "/"))

(defn login-form
  []
  [:div
    [:form {:class "mui-form"
            :on-submit on-submit-form}
      [:div {:class "mui-textfield field"}
        [:input {:type "text"
                :placeholder "Username"
                :autoFocus true
                :on-change #(dispatch
                  [:change-form-username (-> % .-target .-value)])}]]
      [:button {:class "mui-btn mui-btn--raised" :type "submit"}
        "Save"]
      ]])

(defn main-content
  []
    [:div
      [:h2 "Profile"]
      (login-form)
    ])

(defn render
  []
  (page-layout main-content))
