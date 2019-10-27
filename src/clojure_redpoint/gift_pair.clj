(ns clojure-redpoint.gift-pair
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

(defn get-givee-in-gift-pair
  "Returns a givee given a gift pair"
  [g-pair]
  (g-pair :givee))
(s/fdef get-givee-in-gift-pair
        :args (s/cat :g-pair :unq/gift-pair)
        :ret (s/or :found ::givee
                   :not-found nil?))

(defn get-giver-in-gift-pair
  "Returns a giver given a gift pair"
  [g-pair]
  (g-pair :giver))
(s/fdef get-giver-in-gift-pair
        :args (s/cat :g-pair :unq/gift-pair)
        :ret (s/or :found ::giver
                   :not-found nil?))

(ostest/instrument)
