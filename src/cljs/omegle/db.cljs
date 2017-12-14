(ns omegle.db
  (:require [cljs.reader]
            [cljs.spec.alpha :as spec]
            [re-frame.core :as rf]))


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
  {:title "Title here"
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
