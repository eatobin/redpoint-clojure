(ns clojure-redpoint.gift-history
  (:require [clojure-redpoint.domain :as dom]
            [clojure-redpoint.gift-pair :as gp]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::gift-year (s/and int? #(> % -1)))

(defn gift-history-add-year
  "Adds a new placeholder year to the end of a player's gift history"
  [g-hist plr-key]
  (conj g-hist (gp/->Gift-Pair plr-key plr-key)))
(s/fdef gift-history-add-year
        :args (s/cat :g-hist ::dom/gift-history
                     :plr-key ::dom/player-key)
        :ret ::dom/gift-history)

(defn gift-history-update-gift-history
  "Returns a gift history with the provided gift pair at the supplied year"
  [g-hist g-year g-pair]
  (assoc g-hist g-year g-pair))
(s/fdef gift-history-update-gift-history
        :args (s/and
                (s/cat :g-hist ::dom/gift-history
                       :g-year ::gift-year
                       :g-pair ::dom/gift-pair)
                #(< (:g-year %) (count (:g-hist %))))
        :ret ::dom/gift-history)

(ostest/instrument)
