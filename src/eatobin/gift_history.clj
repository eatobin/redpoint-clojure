(ns eatobin.gift-history
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [eatobin.domain :as dom]
            [eatobin.json-utilities :refer [json-utilities-my-value-reader]]
            [orchestra.spec.test :as ostest]))

(defn gift-history-json-string-to-gift-history
  [json-string]
  (json/read-str json-string
                 :value-fn json-utilities-my-value-reader
                 :key-fn keyword))
(s/fdef gift-history-json-string-to-gift-history
  :args (s/cat :json-string ::dom/json-string)
  :ret :unq/gift-history)

(defn gift-history-add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [player-key gift-history]
  (conj gift-history {:givee player-key :giver player-key}))
(s/fdef gift-history-add-year
  :args (s/cat :player-key ::dom/player-key
               :gift-history :unq/gift-history)
  :ret :unq/gift-history)

(defn gift-history-update-gift-history
  "Returns a gift history with the provided gift pair at the supplied year"
  [gift-year gift-pair gift-history]
  (assoc gift-history gift-year gift-pair))
(s/fdef gift-history-update-gift-history
  :args (s/and
         (s/cat :gift-year ::dom/gift-year
                :gift-pair :unq/gift-pair
                :gift-history :unq/gift-history)
         #(< (:gift-year %) (count (:gift-history %))))
  :ret :unq/gift-history)

(ostest/instrument)
