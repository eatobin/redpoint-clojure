(ns clojure-redpoint.rules
  (:require [clojure-redpoint.domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]
            [clojure.repl :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]))

(defn givee-not-self? [plr-sym givee]
  "Test 1 - not giving to yourself"
  (not= plr-sym givee))
(s/fdef givee-not-self?
        :args (s/cat :plr-sym ::dom/giver :givee ::dom/givee)
        :ret boolean?)

(defn givee-not-recip? [plr-sym givee g-year plrs-map]
  "Test 2 - not giving to the person who is giving to you"
  (let [recip (get-givee-in-roster plrs-map givee g-year)]
    (not= plr-sym recip)))
(s/fdef givee-not-recip?
        :args (s/cat :plr-sym ::dom/giver :givee ::dom/givee
                     :g-year ::dom/g-year :plrs-map ::dom/plr-map)
        :ret boolean?)

(defn givee-not-repeat? [plr-sym givee g-year plrs-map]
  "Test 3 - not giving to someone you have given to in the past 3 years"
  (let [past (filter #(>= % 0)
                     (range (- g-year 1) (- g-year 4) -1))
        ge-y (partial get-givee-in-roster plrs-map plr-sym)
        ge-in-yrs (into [] (map ge-y past))]
    (not-any? #{givee} ge-in-yrs)))
(s/fdef givee-not-repeat?
        :args (s/cat :plr-sym ::dom/giver :givee ::dom/givee
                     :g-year ::dom/g-year :plrs-map ::dom/plr-map)
        :ret boolean?)

(ostest/instrument)
