(ns redpoint.rules
  (:require [redpoint.domain :as dom]
            [redpoint.players :as plrs]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn givee-not-self?
  "Test 1 - not giving to yourself"
  [self-key givee]
  (not= self-key givee))
(s/fdef givee-not-self?
        :args (s/cat :self-key ::dom/player-key
                     :givee ::dom/givee)
        :ret boolean?)

(defn givee-not-recip?
  "Test 2 - not giving to the person who is giving to you"
  [self-key givee g-year players]
  (let [recip (plrs/players-get-givee players givee g-year)]
    (not= self-key recip)))
(s/fdef givee-not-recip?
        :args (s/cat :self-key ::dom/player-key
                     :givee ::dom/givee
                     :g-year ::dom/gift-year
                     :players :unq/players)
        :ret boolean?)

(defn givee-not-repeat?
  "Test 3 - not giving to someone you have given to in the past 3 years"
  [self-key givee g-year players]
  (let [past (filter #(>= % 0)
                     (range (- g-year 1) (- g-year 4) -1))
        ge-y (partial plrs/players-get-givee players self-key)
        ge-in-yrs (into [] (map ge-y past))]
    (not-any? #{givee} ge-in-yrs)))
(s/fdef givee-not-repeat?
        :args (s/cat :self-key ::dom/player-key
                     :givee ::dom/givee
                     :g-year ::dom/gift-year
                     :players :unq/players)
        :ret boolean?)

(ostest/instrument)
