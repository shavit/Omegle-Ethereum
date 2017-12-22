(ns omegle.db
  (:require [cljs.reader]
            [cljs.spec.alpha :as spec]
            [re-frame.core :as rf]
            [cljs-web3.core :as web3]))


;; ------------------------------
;; Specification

(spec/def ::id int?)
(spec/def ::title string?)
(spec/def ::body string?)
(spec/def ::created string?)
(spec/def ::message
  (spec/keys :req-un [::id ::body ::created]))
(spec/def ::messages
  (spec/and
    (spec/map-of ::id ::message)
    #(instance? PersistentTreeMap %)
    ))

(spec/def ::video-frame string?)
(spec/def ::video
  (spec/keys :req-un [::id ::video-frame]))
(spec/def ::videos
  (spec/and
    (spec/map-of ::id ::video)))

(spec/def ::db
  (spec/keys :req-un
    [::title ::messages ::videos]
    ))

;; ------------------------------
;; Default values

(def default-db
  {:settings {}
    :accounts {}
    :m-addresses []
    :web3 (or (aget js/window "web3")
              (if goog.DEBUG
                (web3/create-web3 "http://localhost:8545/")
                (web3/create-web3 "https://morden.infura.io/metamask")))
    :provides-web3? (or (aget js/window "web3") goog.DEBUG)
    :contract {:name "Chat"
              :abi nil
              :bin nil
              :instance nil
              :address "0xd499541fcd1f122e933577a4971ebc2502385353"}

    :title "Title here"
    :video-player-source ""
    :messages (sorted-map)
    :videos (sorted-map)})

;; ------------------------------
;; localStorage

(def ls-key "omegle-reframe")
(defn omegle->local-store
  "Puts db into local storage"
  [params]
  (.setItem js/localStorage ls-key (str params)))

;; ------------------------------
;; Handler

(rf/reg-cofx
  :local-store-omegle
  (fn [cofx _]
    (assoc cofx :local-store-omegle
      (into (sorted-map)
        (some->> (.getItem js/localStorage ls-key)
        (cljs.reader/read-string)
        )))))
