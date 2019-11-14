(ns clojure-redpoint.rules
  (:require [clojure-redpoint.gift-pair :as gp]
            [clojure-redpoint.gift-history :as gh]
            [clojure-redpoint.players :as plrs]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn givee-not-self? [plr-key givee]
  "Test 1 - not giving to yourself"
  (not= plr-key givee))
(s/fdef givee-not-self?
        :args (s/cat :plr-key ::gh/player-key
                     :givee ::gp/givee)
        :ret boolean?)

(defn givee-not-recip? [plr-key givee g-year players]
  "Test 2 - not giving to the person who is giving to you"
  (let [recip (plrs/get-giv-ee-er-players players givee :ee g-year)]
    (not= plr-key recip)))
(s/fdef givee-not-recip?
        :args (s/cat :plr-key ::gh/player-key
                     :givee ::gp/givee
                     :g-year ::gh/gift-year
                     :players :unq/players)
        :ret boolean?)

(defn givee-not-repeat? [plr-key givee g-year players]
  "Test 3 - not giving to someone you have given to in the past 3 years"
  (let [past (filter #(>= % 0)
                     (range (- g-year 1) (- g-year 4) -1))
        ge-y (partial plrs/get-giv-ee-er-players players plr-key :ee)
        ge-in-yrs (into [] (map ge-y past))]
    (not-any? #{givee} ge-in-yrs)))
(s/fdef givee-not-repeat?
        :args (s/cat :plr-key ::gh/player-key
                     :givee ::gp/givee
                     :g-year ::gh/gift-year
                     :players :unq/players)
        :ret boolean?)

(ostest/instrument)
