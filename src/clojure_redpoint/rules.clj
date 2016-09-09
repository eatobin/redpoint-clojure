(ns clojure-redpoint.rules
  (:require [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn givee-not-self? [plr-sym givee]
  (not= plr-sym givee))

(defn givee-not-recip? [plr-sym givee g-year plrs-map]
  (let [recip (get-givee-in-roster givee plrs-map g-year)]
    (not= plr-sym recip)))
;
;(defn givee-not-repeat? [gr ge y]
;  (let [past (filter #(>= % 0)
;                     (range (- y 1) (- y 4) -1))
;        ge-y (partial get-givee-code gr)
;        ge-in-yrs (into [] (map ge-y past))]
;    (not-any? #{ge} ge-in-yrs)))
