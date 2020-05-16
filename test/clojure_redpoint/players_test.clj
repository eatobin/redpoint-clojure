(ns clojure-redpoint.players-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.players :as plrs]
            [clojure.spec.alpha :as s]))

(def players {:PauMcc {:player-name  "Paul McCartney",
                       :gift-history [{:giver :JohLen, :givee :GeoHar}]},
              :GeoHar {:player-name  "George Harrison",
                       :gift-history [{:giver :PauMcc, :givee :RinSta}]},
              :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
              :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(def extended-players {:PauMcc {:player-name  "Paul McCartney",
                                :gift-history [{:giver :JohLen, :givee :GeoHar} {:givee :PauMcc, :giver :PauMcc}]},
                       :GeoHar {:player-name  "George Harrison",
                                :gift-history [{:giver :PauMcc, :givee :RinSta} {:givee :GeoHar, :giver :GeoHar}]},
                       :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc} {:givee :JohLen, :giver :JohLen}]},
                       :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen} {:givee :RinSta, :giver :RinSta}]}})

;(def new-bee-players {:PauMcc {:player-name  "Paul McCartney",
;                               :gift-history [{:giver :JohLen, :givee :GeoHar}]},
;                      :GeoHar {:player-name  "George Harrison",
;                               :gift-history [{:giver :PauMcc, :givee :RinSta}]},
;                      :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                      :RinSta {:player-name "New Bee", :gift-history [{:giver :NewBee, :givee :NewBee}]}})

(s/conform :unq/players
           players)

(deftest get-player-name-test
  (is (= "George Harrison"
         (plrs/get-player-name players :GeoHar)))
  (is (nil?
        (plrs/get-player-name players :GeoHarX))))
(s/conform (s/or :found ::plrs/player-name
                 :not-found nil?)
           (plrs/get-player-name players :GeoHar))
(s/conform (s/or :found ::plrs/player-name
                 :not-found nil?)
           (plrs/get-player-name players :GeoHarX))

(deftest add-year-test
  (is (= extended-players
         (plrs/add-year players))))
(s/conform :unq/players
           (plrs/add-year players))

(deftest get-givee-giver-test
  (is (= :RinSta
         (plrs/get-givee players :GeoHar 0)))
  (is (= :PauMcc
         (plrs/get-giver players :GeoHar 0))))
(s/conform ::plrs/givee
           (plrs/get-givee players :GeoHar 0))
(s/conform ::plrs/giver
           (plrs/get-giver players :GeoHar 0))

;(deftest get-player-pass-test
;  (is (= {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}
;         (plrs/get-player players :JohLen))))
;(s/conform (s/or :found :unq/player
;                 :not-found nil?)
;           (plrs/get-player players :JohLen))
;
;(deftest get-player-fail-test
;  (is (= nil
;         (plrs/get-player players :JohLenX))))
;(s/conform (s/or :found :unq/player
;                 :not-found nil?)
;           (plrs/get-player players :JohLenX))
;
;(deftest set-player-test
;  (is (= new-bee-players
;         (plrs/set-player
;           players
;           :RinSta {:player-name "New Bee", :gift-history [{:giver :NewBee, :givee :NewBee}]}))))
;(s/conform :unq/players
;           (plrs/set-player
;             players
;             :RinSta {:player-name "New Bee", :gift-history [{:giver :NewBee, :givee :NewBee}]}))
;

;
;
;

;
;(deftest set-giv-ee-er-players-test
;  (is (= {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
;          :GeoHar {:player-name "George Harrison", :gift-history [{:giver :PauMcc, :givee :you}]},
;          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
;         (plrs/set-giv-ee-er-players players :GeoHar 0 :you :ee)))
;  (is (= {:PauMcc {:player-name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
;          :GeoHar {:player-name "George Harrison", :gift-history [{:giver :you, :givee :RinSta}]},
;          :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;          :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
;         (plrs/set-giv-ee-er-players players :GeoHar 0 :you :er))))
;(s/conform :unq/players
;           (plrs/set-giv-ee-er-players players :GeoHar 0 :you :ee))
;(s/conform :unq/players
;           (plrs/set-giv-ee-er-players players :GeoHar 0 :you :er))
