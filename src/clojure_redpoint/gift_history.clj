(ns clojure-redpoint.gift-history
  (:require [clojure-redpoint.gift-pair]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))

(defn add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [g-hist plr-key]
  (conj g-hist {:givee plr-key, :giver plr-key}))
(s/fdef add-year
        :args (s/cat :g-hist :unq/gift-history
                     :plr-key keyword?)
        :ret :unq/gift-history)

(defn get-gift-pair
  "Returns a gift pair given a gift history and a gift year"
  [g-hist g-year]
  (g-hist g-year))
(s/fdef get-gift-pair
        :args (s/and
                (s/cat :g-hist :unq/gift-history
                       :g-year (s/and int? #(> % -1)))
                #(< (:g-year %) (count (:g-hist %))))
        :ret :unq/gift-pair)

(ostest/instrument)
