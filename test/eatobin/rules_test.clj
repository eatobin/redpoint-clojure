(ns eatobin.rules-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]
            [eatobin.players :refer [players-add-year
                                     players-update-my-givee]]
            [eatobin.rules :refer [rules-givee-not-self?
                                   rules-givee-not-recip?
                                   rules-givee-not-repeat?]]))

(def rules-test-players {:PauMcc {:player-name  "Paul McCartney",
                                  :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                         :GeoHar {:player-name  "George Harrison",
                                  :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                         :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                         :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(def extended ((comp players-add-year
                     players-add-year
                     players-add-year
                     players-add-year)
               rules-test-players))

(def beatles-plus-4
  (->> (players-update-my-givee :RinSta :GeoHar 1 extended)
       (players-update-my-givee :RinSta :PauMcc 2)
       (players-update-my-givee :RinSta :EriTob 3)
       (players-update-my-givee :RinSta :KarLav 4)))

(deftest rules-givee-not-self-true-test
  (is (= true
         (rules-givee-not-self? :RinSta :GeoHar))))
(deftest rules-givee-not-self-false-test
  (is (= false
         (rules-givee-not-self? :RinSta :RinSta))))
(s/conform boolean?
           (rules-givee-not-self? :RinSta :GeoHar))

(deftest rules-givee-not-recip-true-test
  (is (= true
         (rules-givee-not-recip? :RinSta :JohLen 0 rules-test-players))))
(deftest rules-givee-not-recip-false-test
  (is (= false
         (rules-givee-not-recip? :RinSta :GeoHar 0 rules-test-players))))
(s/conform boolean?
           (rules-givee-not-recip? :RinSta :JohLen 0 rules-test-players))

(deftest rules-givee-not-repeat-test
  (is (= false
         (rules-givee-not-repeat? :RinSta :JohLen 2 beatles-plus-4)))
  (is (= false
         (rules-givee-not-repeat? :RinSta :GeoHar 2 beatles-plus-4)))
  (is (= true
         (rules-givee-not-repeat? :RinSta :KarLav 2 beatles-plus-4)))
  (is (= true
         (rules-givee-not-repeat? :RinSta :JohLen 5 beatles-plus-4)))
  (is (= true
         (rules-givee-not-repeat? :RinSta :GeoHar 5 beatles-plus-4)))
  (is (= false
         (rules-givee-not-repeat? :RinSta :PauMcc 5 beatles-plus-4)))
  (is (= false
         (rules-givee-not-repeat? :RinSta :EriTob 5 beatles-plus-4)))
  (is (= false
         (rules-givee-not-repeat? :RinSta :KarLav 5 beatles-plus-4))))
(s/conform boolean?
           (rules-givee-not-repeat? :RinSta :JohLen 2 beatles-plus-4))
