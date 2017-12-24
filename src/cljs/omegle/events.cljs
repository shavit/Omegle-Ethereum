(ns omegle.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx
              path trim-v after debug dispatch]]
            [secretary.core :as secretary :include-macros true]
            [omegle.db :refer [default-db omegle->local-store]]
            [cljs.spec.alpha :as spec]
            [ajax.core :refer [GET POST json-response-format]]
            [madvas.re-frame.web3-fx]
            [day8.re-frame.http-fx]
            [cljs-web3.core :as web3]
            [cljs-web3.eth :as web3-eth]
            [cljs-web3.personal :as web3-personal]
            [cljsjs.web3])
  (:require-macros
            [cljs.core.async.macros :refer [go go-loop]]))

(def eth-account (atom "0xB5B684cbD6fEa6193730556Da98cDB34d78C4Cb3"))
(def gas-limit 60000)

(def interceptors [#_(when ^boolean js/goog.DEBUG debug)
                   trim-v])

(reg-event-fx
  :eth/load-account
  (fn [{:keys [db]} [contracts]]

    (.log js/console "---> Load account")
    (.log js/console db)
    (.log js/console contracts)

    (merge
      {:db default-db
        :http-xhrio {:method :get
                    :uri "/contracts/chat.json"
                    :timeout 3000
                    :response-format (json-response-format {:keywords? true})
                    :on-success [:contract/loaded]
                    :on-failure [:log-error]}}
      (when (:provides-web3? default-db)
        {:web3-fx.blockchain/fns
          {:web3 (:web3 default-db)
            :fns [
              [web3-eth/accounts
              [:blockchain/addresses-loaded]
              [:log-error]]
            ]}}))
    ))

(reg-event-fx
  :blockchain/addresses-loaded
  interceptors
  (fn [{:keys [db]} [addresses]]
    (.log js/console "---> Addresses loaded")
    (println (first addresses))
    {:db (-> db
          (assoc db :m-addresses addresses)
          (assoc db :active-address (first addresses)))
    :web3-fx.blockchain/balances
      {:web3 (:web3 db)
        :addresses addresses
        :watch? true
        :db-path [:blockchain :balances]
        :blockchain-filter-opts "latest"
        :on-success [:blockchain/address-balance-loaded]
        :on-error [:log-error]
        }}
    ))

(reg-event-fx
  :contract/loaded
  interceptors
  (fn [{:keys [db]} [abi]]
    (.log js/console "---> Contract loaded")
    (let [web3 (:web3 db)
          contract-instance (web3-eth/contract-at web3 abi (:address (:contract db)))]
      {:db (assoc-in db [:contract :instance] contract-instance)

        :web3-fx.contract/events
        {:db db
          :db-path [:contract :events]
          :events [
            [contract-instance
              :on-balance-update
              :on-balance-update
              {}
              {}
              [:contract/on-balance-loaded]
              [:log-error]]
            ]}

        :web3-fx.blockchain/fns
         {:fns [
            [contract-instance
              :get-balance
              []
              [:blockchain/address-balance-loaded]
              [:log-error]]
            ]}

          })
    ))

(defn eth->tokens
  [eth]
  (* eth 100))
(reg-event-db
  :contract/on-balance-loaded
  interceptors
  (fn [db [v]]
    (println "---> :contract/on-balance-loaded")
    (assoc db :tokens (eth->tokens
      (-> v :balance int)))
    ))

(reg-event-db
  :blockchain/address-balance-loaded
  interceptors
  (fn [db [_ address]]
    (.log js/console "---> :blockchain/address-balance-loaded")
    (println address)
    (println (-> db :contract :address))
    (assoc db :active-address address)
  ))

(reg-event-fx
  :log-error
  interceptors
  (fn [_ [err]]
    (.error js/console err)
    ))

(defn start-webcam-preview
  [stream]
  (dispatch [:video-preview-source (.createObjectURL js/window.URL stream)])
  )
(reg-event-fx
  :start-webcam-preview
  (fn [_ _]
    (.then
      (.getUserMedia
        (-> js/window .-navigator .-mediaDevices)
        #js{:audio false :video true})
      #(start-webcam-preview %))
    nil))

(reg-event-db
  :change-form-tokens
  (fn [db [_ v]]
    (assoc-in db [:forms :tokens] v)))

(reg-event-fx
  :update-form-tokens
  interceptors
  (fn [{:keys [db]}]
    (println "---> :update-form-tokens")
    (println (-> db :forms :tokens))
    (println (-> db :active-address))
    (println db)
    {:web3-fx.contract/state-fns
      {:web3 (:web3 db)
        :db-path [:contract :transaction-receipt-filter]
        :fns [
          [(-> db :contract :address)
            :set-balance 2121
            {:gas gas-limit
              :from (-> db :m-addresses)
              :on-success [:blockchain/address-balance-loaded]
              :on-error [:log-error]}]
          ]}}
    ))

(reg-event-db
  :change-form-username
  (fn [db [_ v]]
    (assoc-in db [:forms :username] v)))

(reg-event-db
  :update-form-username
  (fn [db [_ v]]
    ; (println "Dispatch route")
    ; (secretary/dispatch! "/")
    (assoc-in db [:user :username]
      (-> db :forms :username))))

(reg-event-db
  :video-preview-source
  (fn [db [_ url]]
    (assoc-in db [:video-preview :src]
      url)))

(reg-event-db
  :video-player-source
  (fn [db [_ url]]
    (assoc db :video-player-source url)))

(reg-event-db
  :set-token-counter
  (fn [db [_ v]]
    (assoc db :token-counter v :token-counter-started true)))

(reg-event-db
  :stop-token-counter
  (fn [db _]
    (.clearInterval js/window
      (:token-counter db) (fn []))
    (assoc db :token-counter nil :token-counter-started false)))

(reg-event-db
  :update-tokens
  (fn [db [_ v]]
    (let [tokens
      (or (:tokens db) 0)
      new-value (+ tokens v)]
      (assoc db :tokens new-value))))

(reg-event-db
  :change-form-chat-message
  (fn [db [_ v]]
    (assoc-in db [:forms :chat :message] v)))

(defn create-message
  [db]
  (let [messages (-> db :chat :messages)]
    {:id (if messages (count messages) 0)
      :username (-> db :user :username)
      :body (-> db :forms :chat :message)}
      ))

(reg-event-db
  :update-form-chat-message
  (fn [db _]
    (let [messages
        (concat (-> db :chat :messages)
        [(create-message db)])]
      (assoc-in db [:chat :messages] messages))))
