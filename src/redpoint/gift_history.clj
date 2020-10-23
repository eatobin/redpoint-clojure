(ns redpoint.gift-history
  (:require [redpoint.domain :as dom]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn gift-history-add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [g-hist plr-key]
  (conj g-hist {:givee plr-key :giver plr-key}))
(s/fdef gift-history-add-year
        :args (s/cat :g-hist :unq/gift-history
                     :plr-key ::dom/player-key)
        :ret :unq/gift-history)

(defn gift-history-update-gift-history
  "Returns a gift history with the provided gift pair at the supplied year"
  [g-hist g-year g-pair]
  (assoc g-hist g-year g-pair))
(s/fdef gift-history-update-gift-history
        :args (s/and
                (s/cat :g-hist :unq/gift-history
                       :g-year ::dom/gift-year
                       :g-pair :unq/gift-pair)
                #(< (:g-year %) (count (:g-hist %))))
        :ret :unq/gift-history)

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn gift-history-json-string-to-Gift-History [gh-string]
  (json/read-str gh-string
                 :value-fn my-value-reader
                 :key-fn keyword))
(s/fdef gift-history-json-string-to-Gift-History
        :args (s/cat :gh-string string?)
        :ret :unq/gift-history)

(ostest/instrument)
