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

(def gp (Gift-Pair. :me :you))
(def gp2 (->Gift-Pair :now :then))

(s/conform :unq/gift-pair
           gp)

(s/explain :unq/gift-pair
           gp2)

(ostest/instrument)
