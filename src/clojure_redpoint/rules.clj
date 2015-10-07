(ns clojure-redpoint.rules
  (:require [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn givee-not-self [gr ge]
  (not= gr ge))

(defn givee-not-recip [gr ge y]
  (let [ge-giving-to (get-givee-code ge y)]
    (not= gr ge-giving-to)))

(defn givee-not-repeat [gr ge y]
  (let [past4 (range (- y 1) (- y 4) -1)
        past (filter #(>= % 0) past4)
        gr-y (partial get-givee-code gr)
        ge-in-yrs (into [] (map gr-y past))]
    (not-any? #{ge} ge-in-yrs)))
