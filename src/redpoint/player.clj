(ns redpoint.player
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn player-update-gift-history
  "Sets a gift history into the provided player"
  [player g-hist]
  (assoc player :gift-history g-hist))
(s/fdef player-update-gift-history
        :args (s/cat :player :unq/player
                     :g-hist :unq/gift-history)
        :ret :unq/player)

(defn- my-key-reader
  [key]
  (cond
    (= key "playerName") :player-name
    (= key "giftHistory") :gift-history
    :else (keyword key)))

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn player-json-string-to-Player [plr-string]
  (json/read-str plr-string
                 :value-fn my-value-reader
                 :key-fn my-key-reader))
(s/fdef player-json-string-to-Player
        :args (s/cat :plr-string string?)
        :ret :unq/player)

(ostest/instrument)
