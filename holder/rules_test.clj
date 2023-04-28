(ns redpoint.rules-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]
            [redpoint.domain]
            [redpoint.players :as plrs]
            [redpoint.rules :as rule]))

(def players {:PauMcc {:player-name  "Paul McCartney",
                       :gift-history [{:giver :JohLen, :givee :GeoHar}]},
              :GeoHar {:player-name  "George Harrison",
                       :gift-history [{:giver :PauMcc, :givee :RinSta}]},
              :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
              :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(def extended ((comp plrs/players-add-year
                     plrs/players-add-year
                     plrs/players-add-year
                     plrs/players-add-year)
               players))

(def beatles-plus-4 (plrs/players-update-givee
                      (plrs/players-update-givee
                        (plrs/players-update-givee
                          (plrs/players-update-givee extended :RinSta 1 :GeoHar) :RinSta 2 :PauMcc) :RinSta 3 :EriTob) :RinSta 4 :KarLav))

(deftest givee-not-self-test
  (is (= true
         (rule/givee-not-self? :RinSta :GeoHar)))
  (is (= false
         (rule/givee-not-self? :RinSta :RinSta))))
(s/conform boolean?
           (rule/givee-not-self? :RinSta :GeoHar))

(deftest givee-not-recip-test
  (is (= true
         (rule/givee-not-recip? :RinSta :JohLen 0 players)))
  (is (= false
         (rule/givee-not-recip? :RinSta :GeoHar 0 players))))
(s/conform boolean?
           (rule/givee-not-recip? :RinSta :JohLen 0 players))

(deftest givee-not-repeat-test
  (is (= false
         (rule/givee-not-repeat? :RinSta :JohLen 2 beatles-plus-4)))
  (is (= false
         (rule/givee-not-repeat? :RinSta :GeoHar 2 beatles-plus-4)))
  (is (= true
         (rule/givee-not-repeat? :RinSta :KarLav 2 beatles-plus-4)))
  (is (= true
         (rule/givee-not-repeat? :RinSta :JohLen 5 beatles-plus-4)))
  (is (= true
         (rule/givee-not-repeat? :RinSta :GeoHar 5 beatles-plus-4)))
  (is (= false
         (rule/givee-not-repeat? :RinSta :PauMcc 5 beatles-plus-4)))
  (is (= false
         (rule/givee-not-repeat? :RinSta :EriTob 5 beatles-plus-4)))
  (is (= false
         (rule/givee-not-repeat? :RinSta :KarLav 5 beatles-plus-4))))
(s/conform boolean?
           (rule/givee-not-repeat? :RinSta :JohLen 2 beatles-plus-4))
