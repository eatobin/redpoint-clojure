(ns eatobin.rules-test
  (:require [eatobin.gift-pair :as gp]
            [eatobin.player :as plr]
            [eatobin.players :as plrs]
            [clojure.test :refer [deftest is]]
            [eatobin.rules :as rule]
            [eatobin.roster]
            [clojure.spec.alpha :as s]))

(def players {:PauMcc (plr/map->Player {:player-name  "Paul McCartney",
                                        :gift-history [(gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})]}),
              :GeoHar (plr/map->Player {:player-name  "George Harrison",
                                        :gift-history [(gp/map->Gift-Pair {:giver :PauMcc, :givee :RinSta})]}),
              :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:giver :RinSta, :givee :PauMcc})]}),
              :RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [(gp/map->Gift-Pair {:giver :GeoHar, :givee :JohLen})]})
              :EriTob (plr/map->Player {:player-name "Eric Tobin", :gift-history [(gp/map->Gift-Pair {:giver :PauMcc, :givee :KarLav})]}),
              :KarLav (plr/map->Player {:player-name  "Karen Lavengood",
                                        :gift-history [(gp/map->Gift-Pair {:giver :EriTob, :givee :RinSta})]})})

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
         (rule/givee-not-recip? :RinSta :KarLav 0 players))))
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
