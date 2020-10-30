(ns redpoint.players-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.domain :as dom]
            [redpoint.players :as plrs]
            [clojure.spec.alpha :as s]))

(def json-string-Players "{\"PauMcc\":{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"playerName\":\"George Harrison\",\"giftHistory\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"playerName\":\"John Lennon\",\"giftHistory\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"playerName\":\"Ringo Starr\",\"giftHistory\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}}")
(def players (plrs/players-json-string-to-Players json-string-Players))

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

(s/conform :unq/players
           players)

(deftest players-update-player
  (is (= new-bee-players
         (plrs/players-update-player players :RinSta new-bee))))

(deftest get-player-name-test
  (is (= "George Harrison"
         (plrs/players-get-player-name players :GeoHar)))
  (is (nil?
       (plrs/players-get-player-name players :GeoHarX))))
(s/conform (s/or :found ::dom/player-name
                 :not-found nil?)
           (plrs/players-get-player-name players :GeoHar))
(s/conform (s/or :found ::dom/player-name
                 :not-found nil?)
           (plrs/players-get-player-name players :GeoHarX))

(deftest players-add-year-test
  (is (= extended-players
         (plrs/players-add-year players))))
(s/conform :unq/players
           (plrs/players-add-year players))

(deftest players-get-givee-giver-test
  (is (= :RinSta
         (plrs/players-get-givee players :GeoHar 0)))
  (is (= :PauMcc
         (plrs/players-get-giver players :GeoHar 0))))
(s/conform ::dom/givee
           (plrs/players-get-givee players :GeoHar 0))
(s/conform ::dom/giver
           (plrs/players-get-giver players :GeoHar 0))

(deftest set-givee-giver-test
  (is (= {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
          :GeoHar {:player-name "George Harrison", :gift-history [{:giver :PauMcc, :givee :you}]},
          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
         (plrs/players-update-givee players :GeoHar 0 :you)))
  (is (= {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
          :GeoHar {:player-name "George Harrison", :gift-history [{:giver :you, :givee :RinSta}]},
          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
         (plrs/players-update-giver players :GeoHar 0 :you))))
(s/conform :unq/players
           (plrs/players-update-givee players :GeoHar 0 :you))
(s/conform :unq/players
           (plrs/players-update-giver players :GeoHar 0 :you))

(s/conform :unq/players
           (plrs/players-json-string-to-Players json-string-Players))
