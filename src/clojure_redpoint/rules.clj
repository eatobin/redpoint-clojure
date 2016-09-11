(ns clojure-redpoint.rules
  (:require [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn givee-not-self? [plr-sym givee]
  (not= plr-sym givee))

(defn givee-not-recip? [plr-sym givee g-year plrs-map]
  (let [recip (get-givee-in-roster givee plrs-map g-year)]
    (not= plr-sym recip)))

(defn givee-not-repeat? [plr-sym givee g-year plrs-map]
  (let [past (filter #(>= % 0)
                     (range (- g-year 1) (- g-year 4) -1))
        ge-y (partial get-givee-in-roster plr-sym plrs-map)
        ge-in-yrs (into [] (map ge-y past))]
    (not-any? #{givee} ge-in-yrs)))
