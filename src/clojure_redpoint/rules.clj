(ns clojure-redpoint.rules
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.gift-history :as gh]
            [clojure-redpoint.roster :as plrs]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn givee-not-self? [self-key givee]
  "Test 1 - not giving to yourself"
  (not= self-key givee))
(s/fdef givee-not-self?
        :args (s/cat :self-key ::gh/player-key
                     :givee ::gp/givee)
        :ret boolean?)

(defn givee-not-recip? [self-key givee g-year players]
  "Test 2 - not giving to the person who is giving to you"
  (let [recip (plrs/get-giv-ee-er-players players givee :ee g-year)]
    (not= self-key recip)))
(s/fdef givee-not-recip?
        :args (s/cat :self-key ::gh/player-key
                     :givee ::gp/givee
                     :g-year ::gh/gift-year
                     :players :unq/players)
        :ret boolean?)

(defn givee-not-repeat? [self-key givee g-year players]
  "Test 3 - not giving to someone you have given to in the past 3 years"
  (let [past (filter #(>= % 0)
                     (range (- g-year 1) (- g-year 4) -1))
        ge-y (partial plrs/get-giv-ee-er-players players self-key :ee)
        ge-in-yrs (into [] (map ge-y past))]
    (not-any? #{givee} ge-in-yrs)))
(s/fdef givee-not-repeat?
        :args (s/cat :self-key ::gh/player-key
                     :givee ::gp/givee
                     :g-year ::gh/gift-year
                     :players :unq/players)
        :ret boolean?)

(ostest/instrument)
