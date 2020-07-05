(ns clojure-redpoint.rules
  (:require [clojure-redpoint.roster :as ros]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn givee-not-self? [self-key givee]
  "Test 1 - not giving to yourself"
  (not= self-key givee))
(s/fdef givee-not-self?
        :args (s/cat :self-key ::ros/player-key
                     :givee ::ros/givee)
        :ret boolean?)

(defn givee-not-recip? [self-key givee g-year players]
  "Test 2 - not giving to the person who is giving to you"
  (let [recip (ros/get-givee players givee g-year)]
    (not= self-key recip)))
(s/fdef givee-not-recip?
        :args (s/cat :self-key ::ros/player-key
                     :givee ::ros/givee
                     :g-year ::ros/gift-year
                     :players :unq/players)
        :ret boolean?)

(defn givee-not-repeat? [self-key givee g-year players]
  "Test 3 - not giving to someone you have given to in the past 3 years"
  (let [past (filter #(>= % 0)
                     (range (- g-year 1) (- g-year 4) -1))
        ge-y (partial ros/get-givee players self-key)
        ge-in-yrs (into [] (map ge-y past))]
    (not-any? #{givee} ge-in-yrs)))
(s/fdef givee-not-repeat?
        :args (s/cat :self-key ::ros/player-key
                     :givee ::ros/givee
                     :g-year ::ros/gift-year
                     :players :unq/players)
        :ret boolean?)

(ostest/instrument)
