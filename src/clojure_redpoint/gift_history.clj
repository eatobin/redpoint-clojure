(ns clojure-redpoint.gift-history
  (:require [clojure-redpoint.gift-pair]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def ::player-key keyword?)
(s/def ::gift-year (s/and int? #(> % -1)))

(defn add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [g-hist plr-key]
  (conj g-hist {:givee plr-key, :giver plr-key}))
(s/fdef add-year
        :args (s/cat :g-hist :unq/gift-history
                     :plr-key ::player-key)
        :ret :unq/gift-history)

(defn get-gift-pair
  "Returns a gift pair given a gift history and a gift year"
  [g-hist g-year]
  (g-hist g-year))
(s/fdef get-gift-pair
        :args (s/and
                (s/cat :g-hist :unq/gift-history
                       :g-year ::gift-year)
                #(< (:g-year %) (count (:g-hist %))))
        :ret :unq/gift-pair)

(defn set-gift-pair
  "Returns a gift history with the provided gift pair at the supplied year"
  [g-hist g-year g-pair]
  (assoc g-hist g-year g-pair))
(s/fdef set-gift-pair
        :args (s/and
                (s/cat :g-hist :unq/gift-history
                       :g-year ::gift-year
                       :g-pair :unq/gift-pair)
                #(< (:g-year %) (count (:g-hist %))))
        :ret :unq/gift-history)

(ostest/instrument)
