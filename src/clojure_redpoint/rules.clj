(ns clojure-redpoint.rules
  (:require [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn givee-not-self [gr ge]
  (not= gr ge))

(defn givee-not-recip [gr ge y]
  (not= gr (get-givee-code ge y)))

(defn givee-not-repeat [gr ge y]
  (let [past (filter #(>= % 0)
                     (range (- y 1) (- y 4) -1))
        ge-y (partial get-givee-code gr)
        ge-in-yrs (into [] (map ge-y past))]
    (not-any? #{ge} ge-in-yrs)))
