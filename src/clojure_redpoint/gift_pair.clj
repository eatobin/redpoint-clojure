(ns clojure-redpoint.gift-pair
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

(defn get-givee
  "Returns a givee given a gift pair"
  [g-pair]
  (g-pair :givee))
(s/fdef get-givee
        :args (s/cat :g-pair :unq/gift-pair)
        :ret (s/or :found ::givee
                   :not-found nil?))

(defn get-giver
  "Returns a giver given a gift pair"
  [g-pair]
  (g-pair :giver))
(s/fdef get-giver
        :args (s/cat :g-pair :unq/gift-pair)
        :ret (s/or :found ::giver
                   :not-found nil?))

(ostest/instrument)
