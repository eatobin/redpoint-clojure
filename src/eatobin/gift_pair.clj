(ns eatobin.gift-pair
  (:require [eatobin.redpoint-domain :as dom]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defrecord Gift-Pair [givee giver])
(s/fdef ->Gift-Pair
        :args (s/cat :givee ::dom/givee
                     :giver ::dom/giver)
        :ret ::dom/gift-pair)

(defn gift-pair-update-givee [gift-pair givee]
  (assoc gift-pair :givee givee))
(s/fdef gift-pair-update-givee
        :args (s/cat :gift-pair ::dom/gift-pair
                     :givee ::dom/givee)
        :ret ::dom/gift-pair)

(defn gift-pair-update-giver [gift-pair giver]
  (assoc gift-pair :giver giver))
(s/fdef gift-pair-update-giver
        :args (s/cat :gift-pair ::dom/gift-pair
                     :giver ::dom/giver)
        :ret ::dom/gift-pair)

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn gift-pair-json-string-to-Gift-Pair [gp-string]
  (let [gift-pair (json/read-str gp-string
                                 :value-fn my-value-reader
                                 :key-fn keyword)]
    (->Gift-Pair (:givee gift-pair)
                 (:giver gift-pair))))
(s/fdef gift-pair-json-string-to-Gift-Pair
        :args (s/cat :gp-string string?)
        :ret ::dom/gift-pair)

(ostest/instrument)
