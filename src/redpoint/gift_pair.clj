(ns redpoint.gift-pair
  (:require [clojure.data.json :as json]
    [clojure.spec.alpha :as s]
    [orchestra.spec.test :as ostest]
    [redpoint.domain :as dom]))

(defn gift-pair-update-givee [gift-pair givee]
  (assoc gift-pair :givee givee))
(s/fdef gift-pair-update-givee
  :args (s/cat :gift-pair :unq/gift-pair
          :givee ::dom/givee)
  :ret :unq/gift-pair)

(defn gift-pair-update-giver [gift-pair giver]
  (assoc gift-pair :giver giver))
(s/fdef gift-pair-update-giver
  :args (s/cat :gift-pair :unq/gift-pair
          :giver ::dom/giver)
  :ret :unq/gift-pair)

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

(ostest/instrument)
