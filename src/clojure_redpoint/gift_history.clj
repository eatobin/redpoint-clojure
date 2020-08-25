(ns clojure-redpoint.gift-history
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def ::player-key keyword?)
(s/def ::gift-year (s/and int? #(> % -1)))

(defn gift-history-add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [g-hist plr-key]
  (conj g-hist (gp/->Gift-Pair plr-key plr-key)))
(s/fdef gift-history-add-year
        :args (s/cat :g-hist ::gift-history
                     :plr-key ::player-key)
        :ret ::gift-history)

(defn gift-history-update-gift-history
  "Returns a gift history with the provided gift pair at the supplied year"
  [g-hist g-year g-pair]
  (assoc g-hist g-year g-pair))
(s/fdef gift-history-update-gift-history
        :args (s/and
                (s/cat :g-hist ::gift-history
                       :g-year ::gift-year
                       :g-pair ::gp/gift-pair)
                #(< (:g-year %) (count (:g-hist %))))
        :ret ::gift-history)

(ostest/instrument)
