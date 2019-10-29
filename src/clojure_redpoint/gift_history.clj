(ns clojure-redpoint.gift-history
  (:require [clojure-redpoint.gift-pair]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))

(defn add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [gh plr-key]
  (conj gh {:givee plr-key, :giver plr-key}))
(s/fdef add-year
        :args (s/cat :gh :unq/gift-history
                     :plr-key keyword?)
        :ret :unq/gift-history)

(ostest/instrument)
