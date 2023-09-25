(ns eatobin.players-test
  (:require [clojure.spec.alpha :as s]
    [clojure.test :refer [deftest is testing]]
    [eatobin.domain :as dom]
    [eatobin.players :refer [players-json-string-to-players
                             players-update-player
                             players-get-player-name
                             players-add-year
                             players-get-my-givee
                             players-get-my-giver
                             players-update-my-givee
                             players-update-my-giver]]))

(def players-test-json-string "{\"PauMcc\":{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"playerName\":\"George Harrison\",\"giftHistory\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"playerName\":\"John Lennon\",\"giftHistory\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"playerName\":\"Ringo Starr\",\"giftHistory\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}}")
(def players-test-players {:PauMcc {:player-name  "Paul McCartney",
                                    :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                           :GeoHar {:player-name  "George Harrison",
                                    :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                           :JohLen {:player-name  "John Lennon",
                                    :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                           :RinSta {:player-name  "Ringo Starr",
                                    :gift-history [{:giver :GeoHar, :givee :JohLen}]}})
(def extended-players {:PauMcc {:player-name  "Paul McCartney",
                                :gift-history [{:giver :JohLen, :givee :GeoHar}
                                               {:givee :PauMcc, :giver :PauMcc}]},
                       :GeoHar {:player-name  "George Harrison",
                                :gift-history [{:giver :PauMcc, :givee :RinSta}
                                               {:givee :GeoHar, :giver :GeoHar}]},
                       :JohLen {:player-name  "John Lennon",
                                :gift-history [{:giver :RinSta, :givee :PauMcc}
                                               {:givee :JohLen, :giver :JohLen}]},
                       :RinSta {:player-name  "Ringo Starr",
                                :gift-history [{:giver :GeoHar, :givee :JohLen}
                                               {:givee :RinSta, :giver :RinSta}]}})

(def new-bee {:player-name "New Bee" :gift-history [{:giver :NewBee, :givee :NewBee}]})

(def new-bee-players {:PauMcc {:player-name  "Paul McCartney",
                               :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                      :GeoHar {:player-name  "George Harrison",
                               :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                      :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                      :RinSta {:player-name "New Bee", :gift-history [{:giver :NewBee, :givee :NewBee}]}})

(deftest json-test
  (testing "players from JSON correctly constructed"
    (is (= players-test-players
          (players-json-string-to-players players-test-json-string)))))
(s/conform :unq/players
  (players-json-string-to-players players-test-json-string))

(deftest players-update-player-test
  (is (= new-bee-players
        (players-update-player :RinSta new-bee players-test-players))))

(deftest get-player-name-pass-test
  (is (= "George Harrison"
        (players-get-player-name :GeoHar players-test-players))))
(s/conform (s/or :found ::dom/player-name
             :not-found nil?)
  (players-get-player-name :GeoHar players-test-players))

(deftest get-player-name-fail-test
  (is (nil?
        (players-get-player-name :GeoHarX players-test-players))))
(s/conform (s/or :found ::dom/player-name
             :not-found nil?)
  (players-get-player-name :GeoHarX players-test-players))

(deftest players-add-year-test
  (is (= extended-players
        (players-add-year players-test-players))))
(s/conform :unq/players
  (players-add-year players-test-players))

(deftest players-get-my-givee-test
  (is (= :RinSta
        (players-get-my-givee :GeoHar players-test-players 0))))
(s/conform ::dom/givee
  (players-get-my-givee :GeoHar players-test-players 0))

(deftest players-get-my-giver-test
  (is (= :PauMcc
        (players-get-my-giver :GeoHar players-test-players 0))))
(s/conform ::dom/giver
  (players-get-my-giver :GeoHar players-test-players 0))

(deftest set-givee-test
  (is (= {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
          :GeoHar {:player-name "George Harrison", :gift-history [{:giver :PauMcc, :givee :you}]},
          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
        (players-update-my-givee :GeoHar :you 0 players-test-players))))
(s/conform :unq/players
  (players-update-my-givee :GeoHar :you 0 players-test-players))

(deftest set-giver-test
  (is (= {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
          :GeoHar {:player-name "George Harrison", :gift-history [{:giver :you, :givee :RinSta}]},
          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
        (players-update-my-giver :GeoHar :you 0 players-test-players))))
(s/conform :unq/players
  (players-update-my-giver :GeoHar :you 0 players-test-players))
