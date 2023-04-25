(ns redpoint.gift-pair
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn gift-pair-json-string-to-Gift-Pair [gp-string]
  (json/read-str gp-string
                 :value-fn my-value-reader
                 :key-fn keyword))
(s/fdef gift-pair-json-string-to-Gift-Pair
        :args (s/cat :gp-string string?)
        :ret :unq/gift-pair)

(defn gift-pair-update-givee [gift-pair givee]
  (assoc gift-pair :givee givee))
(s/fdef gift-pair-update-givee
        :args (s/cat :gift-pair :unq/gift-pair
                     :givee ::givee)
        :ret :unq/gift-pair)

(defn gift-pair-update-giver [gift-pair giver]
  (assoc gift-pair :giver giver))
(s/fdef gift-pair-update-giver
        :args (s/cat :gift-pair :unq/gift-pair
                     :giver ::giver)
        :ret :unq/gift-pair)

(ostest/instrument)
