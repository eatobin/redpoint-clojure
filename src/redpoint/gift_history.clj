(ns redpoint.gift-history
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def ::player-key keyword?)
(s/def ::gift-year (s/and int? #(> % -1)))

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn gift-history-json-string-to-gift-history
  [json-string]
  (json/read-str json-string
                 :value-fn my-value-reader
                 :key-fn keyword))
(s/fdef gift-history-json-string-to-gift-history
        :args (s/cat :gh-string string?)
        :ret :unq/gift-history)

(defn gift-history-add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [gift-history player-key]
  (conj gift-history {:givee player-key :giver player-key}))
(s/fdef gift-history-add-year
        :args (s/cat :g-hist :unq/gift-history
                     :plr-key ::player-key)
        :ret :unq/gift-history)

(defn gift-history-update-gift-history
  "Returns a gift history with the provided gift pair at the supplied year"
  [g-hist g-year g-pair]
  (assoc g-hist g-year g-pair))
(s/fdef gift-history-update-gift-history
        :args (s/and
                (s/cat :g-hist :unq/gift-history
                       :g-year ::gift-year
                       :g-pair :unq/gift-pair)
                #(< (:g-year %) (count (:g-hist %))))
        :ret :unq/gift-history)

(ostest/instrument)
