(ns omegle.views.page-profile
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [secretary.core :as secretary :include-macros true]
            [omegle.views :refer [page-layout]]))

(defn on-submit-form-profile
  [event]
  (.preventDefault event)
  (dispatch [:update-form-username])
  (.pushState js/window.history "" "" "/")
  (secretary/dispatch! "/"))

(defn on-submit-form-tokens
  [event]
  (.preventDefault event))

(defn login-form
  []
  (dispatch [:stop-token-counter])

  [:div
    [:h2 "My Profile"]
    [:form {:class "mui-form"
            :on-submit on-submit-form-profile}
      [:div {:class "mui-textfield field"}
        [:input {:type "text"
                :placeholder "Username"
                :autoFocus true
                :on-change #(dispatch
                  [:change-form-username (-> % .-target .-value)])}]]
      [:button {:class "mui-btn mui-btn--raised" :type "submit"}
        "Save"]
      ]])

(defn tokens-form
  []
  [:div
    [:h2 "Buy tokens"]
    [:form {:class ""
            :on-submit on-submit-form-tokens}
      [:div {:class "mui-textfield field"}
        [:input {:type "text"
                :placeholder "0.00"
                :on-change #(dispatch
                  [:change-form-tokens (-> % .-target .-value)])}]]
      [:button {:class "mui-btn mui-btn--raised" :type "submit"}
        "Buy"]

    ]])

(defn main-content
  []
    [:div
      (login-form)
      (tokens-form)])

(defn render
  []
  (page-layout main-content))
