(ns redpoint.gift-pair
  (:require [clojure.data.json :as json]
            [malli.core :as m]))

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

(defn gift-pair-update-givee
  [givee gift-pair]
  (assoc gift-pair :givee givee))

(defn gift-pair-update-giver
  [giver gift-pair]
  (assoc gift-pair :giver giver))

(def non-empty-string
  (m/schema [:string {:min 1}]))

;; optimized (pure) validation function for best performance
(def et-valid?
  (m/validator
    [:map
     [:x :boolean]
     [:y {:optional true} :int]
     [:z :string]]))
(def good {:x true, :z "kikka"})
(et-valid? {:x true, :y 6.9, :z "kikka"})
(et-valid? good)
; => true

(m/schema? non-empty-string)
; => true

(m/validate non-empty-string "")
; => false

(m/validate non-empty-string "kikka")
; => true

(m/form non-empty-string)
; => [:string {:min 1}]

(m/validate
  [:map [:x :int]]
  {:x 1, :extra "key"})
; => true
