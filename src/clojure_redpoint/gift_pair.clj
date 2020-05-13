(ns clojure-redpoint.gift-pair
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def ::giv keyword?)
(s/def ::ee-er keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

(defn get-giv-ee-er
  "Returns a givEeEr given a gift pair and ee-er"
  [g-pair ee-er]
  (if (= ee-er :ee)
    (g-pair :givee)
    (g-pair :giver)))
(s/fdef get-giv-ee-er
        :args (s/cat :g-pair :unq/gift-pair
                     :ee-er ::ee-er)
        :ret ::giv)

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
