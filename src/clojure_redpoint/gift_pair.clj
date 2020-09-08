(ns clojure-redpoint.gift-pair
  (:require [clojure-redpoint.domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::gift-pair (s/keys :req-un [::dom/givee ::dom/giver]))

(defrecord Gift-Pair [givee giver])
(s/fdef ->Gift-Pair
        :args (s/cat :givee ::dom/givee
                     :giver ::dom/giver)
        :ret ::gift-pair)

(defn gift-pair-update-givee [gift-pair givee]
  (assoc gift-pair :givee givee))
(s/fdef gift-pair-update-givee
        :args (s/cat :gift-pair ::gift-pair
                     :givee ::dom/givee)
        :ret ::gift-pair)

(defn gift-pair-update-giver [gift-pair giver]
  (assoc gift-pair :giver giver))
(s/fdef gift-pair-update-giver
        :args (s/cat :gift-pair ::gift-pair
                     :giver ::dom/giver)
        :ret ::gift-pair)

(ostest/instrument)
