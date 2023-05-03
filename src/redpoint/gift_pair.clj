(ns redpoint.gift-pair
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

; this is a comment in Alabaster
(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn gift-pair-json-string-to-gift-pair
  [json-string]
  (json/read-str json-string
                 :value-fn my-value-reader
                 :key-fn keyword))
(s/fdef gift-pair-json-string-to-gift-pair
        :args (s/cat :gp-string string?)
        :ret :unq/gift-pair)

(defn gift-pair-update-givee
  [givee gift-pair]
  (assoc gift-pair :givee givee))
(s/fdef gift-pair-update-givee
        :args (s/cat :givee ::givee
                     :gift-pair :unq/gift-pair)
        :ret :unq/gift-pair)

(defn gift-pair-update-giver
  [giver gift-pair]
  (assoc gift-pair :giver giver))
(s/fdef gift-pair-update-giver
        :args (s/cat :giver ::giver
                     :gift-pair :unq/gift-pair)
        :ret :unq/gift-pair)

(ostest/instrument)
