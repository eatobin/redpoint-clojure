(ns clojure-redpoint.gift-pair
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def ::giv keyword?)
(s/def ::ee-er keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

(defn get-givee
  "Returns a givee given a gift pair"
  [g-pair]
  (g-pair :givee))
(s/fdef get-givee
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::givee)

(defn get-giver
  "Returns a giver given a gift pair"
  [g-pair]
  (g-pair :giver))
(s/fdef get-giver
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::giver)

(defn set-giv-ee-er
  "Returns a gift pair with updated givEeEr"
  [g-pair giv ee-er]
  (if (= ee-er :ee)
    (assoc g-pair :givee giv)
    (assoc g-pair :giver giv)))
(s/fdef set-giv-ee-er
        :args (s/cat :g-pair :unq/gift-pair
                     :giv ::giv
                     :ee-er ::ee-er)
        :ret :unq/gift-pair)

(ostest/instrument)
