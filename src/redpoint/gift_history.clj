(ns redpoint.gift-history
  (:require [clojure.data.json :as json]))

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

(defn gift-history-add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [gift-history player-key]
  (conj gift-history {:givee player-key :giver player-key}))

(defn gift-history-update-gift-history
  "Returns a gift history with the provided gift pair at the supplied year"
  [g-hist g-year g-pair]
  (assoc g-hist g-year g-pair))
