(ns clojure-redpoint.players-test
  (:require [clojure.test :refer [deftest is]]
            [clojure-redpoint.domain :as dom]
            [clojure-redpoint.player :as plr]
            [clojure-redpoint.players :as plrs]
            [clojure.spec.alpha :as s]
            [clojure-redpoint.gift-pair :as gp]))

(def plain-players {:PauMcc {:player-name  "Paul McCartney",
                             :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                    :GeoHar {:player-name  "George Harrison",
                             :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                    :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                    :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(def players {:PauMcc (plr/map->Player {:player-name  "Paul McCartney",
                                        :gift-history [(gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})]}),
              :GeoHar (plr/map->Player {:player-name  "George Harrison",
                                        :gift-history [(gp/map->Gift-Pair {:giver :PauMcc, :givee :RinSta})]}),
              :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:giver :RinSta, :givee :PauMcc})]}),
              :RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [(gp/map->Gift-Pair {:giver :GeoHar, :givee :JohLen})]})})

(def extended-players {:PauMcc (plr/map->Player {:player-name  "Paul McCartney",
                                                 :gift-history [(gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})
                                                                (gp/map->Gift-Pair {:givee :PauMcc, :giver :PauMcc})]}),
                       :GeoHar (plr/map->Player {:player-name  "George Harrison",
                                                 :gift-history [(gp/map->Gift-Pair {:giver :PauMcc, :givee :RinSta})
                                                                (gp/map->Gift-Pair {:givee :GeoHar, :giver :GeoHar})]}),
                       :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:giver :RinSta, :givee :PauMcc})
                                                                                            (gp/map->Gift-Pair {:givee :JohLen, :giver :JohLen})]}),
                       :RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [(gp/map->Gift-Pair {:giver :GeoHar, :givee :JohLen})
                                                                                            (gp/map->Gift-Pair {:givee :RinSta, :giver :RinSta})]})})

(def new-bee (plr/->Player "New Bee" [(gp/map->Gift-Pair {:giver :NewBee, :givee :NewBee})]))

(def new-bee-players {:PauMcc (plr/map->Player {:player-name  "Paul McCartney",
                                                :gift-history [(gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})]}),
                      :GeoHar (plr/map->Player {:player-name  "George Harrison",
                                                :gift-history [(gp/map->Gift-Pair {:giver :PauMcc, :givee :RinSta})]}),
                      :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:giver :RinSta, :givee :PauMcc})]}),
                      :RinSta (plr/map->Player {:player-name "New Bee", :gift-history [(gp/map->Gift-Pair {:giver :NewBee, :givee :NewBee})]})})

(s/conform ::plrs/players
           players)

(deftest players-update-player
  (is (= new-bee-players
         (plrs/players-update-player players :RinSta new-bee))))

(deftest get-player-name-test
  (is (= "George Harrison"
         (plrs/players-get-player-name players :GeoHar)))
  (is (nil?
        (plrs/players-get-player-name players :GeoHarX))))
(s/conform (s/or :found ::plr/player-name
                 :not-found nil?)
           (plrs/players-get-player-name players :GeoHar))
(s/conform (s/or :found ::plr/player-name
                 :not-found nil?)
           (plrs/players-get-player-name players :GeoHarX))

(deftest players-add-year-test
  (is (= extended-players
         (plrs/players-add-year players))))
(s/conform ::plrs/players
           (plrs/players-add-year players))

(deftest players-get-givee-giver-test
  (is (= :RinSta
         (plrs/players-get-givee players :GeoHar 0)))
  (is (= :PauMcc
         (plrs/players-get-giver players :GeoHar 0))))
(s/conform ::dom/givee
           (plrs/players-get-givee players :GeoHar 0))
(s/conform ::gp/giver
           (plrs/players-get-giver players :GeoHar 0))

(deftest set-givee-giver-test
  (is (= {:PauMcc (plr/map->Player {:player-name "Paul McCartney", :gift-history [(gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})]}),
          :GeoHar (plr/map->Player {:player-name "George Harrison", :gift-history [(gp/map->Gift-Pair {:giver :PauMcc, :givee :you})]}),
          :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:giver :RinSta, :givee :PauMcc})]}),
          :RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [(gp/map->Gift-Pair {:giver :GeoHar, :givee :JohLen})]})}
         (plrs/players-update-givee players :GeoHar 0 :you)))
  (is (= {:PauMcc (plr/map->Player {:player-name "Paul McCartney", :gift-history [(gp/map->Gift-Pair {:giver :JohLen, :givee :GeoHar})]}),
          :GeoHar (plr/map->Player {:player-name "George Harrison", :gift-history [(gp/map->Gift-Pair {:giver :you, :givee :RinSta})]}),
          :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:giver :RinSta, :givee :PauMcc})]}),
          :RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [(gp/map->Gift-Pair {:giver :GeoHar, :givee :JohLen})]})}
         (plrs/players-update-giver players :GeoHar 0 :you))))
(s/conform ::plrs/players
           (plrs/players-update-givee players :GeoHar 0 :you))
(s/conform ::plrs/players
           (plrs/players-update-giver players :GeoHar 0 :you))

(deftest players-plain-player-upgrade-test
  (is (= players
         (plrs/players-plain-player-upgrade plain-players))))
(s/conform ::plrs/players
           (plrs/players-plain-player-upgrade plain-players))
