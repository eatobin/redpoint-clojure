(ns clojure-redpoint.gift-pair
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

(defrecord Gift-Pair [givee giver])
(s/fdef ->Gift-Pair
        :args (s/cat :givee ::givee
                     :giver ::giver)
        :ret :unq/gift-pair)

(defn set-givee [gift-pair givee]
  (assoc gift-pair :givee givee))
(s/fdef set-givee
        :args (s/cat :gift-pair :unq/gift-pair
                     :givee ::givee)
        :ret :unq/gift-pair)

(defn set-giver [gift-pair giver]
  (assoc gift-pair :giver giver))
(s/fdef set-giver
        :args (s/cat :gift-pair :unq/gift-pair
                     :giver ::giver)
        :ret :unq/gift-pair)

(ostest/instrument)
