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

(s/conform :unq/players
           players)

(deftest get-player-pass-test
  (is (= {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}
         (plrs/get-player players :JohLen))))
(s/conform (s/or :found :unq/player
                 :not-found nil?)
           (plrs/get-player players :JohLen))

(deftest get-player-fail-test
  (is (= nil
         (plrs/get-player players :JohLenX))))
(s/conform (s/or :found :unq/player
                 :not-found nil?)
           (plrs/get-player players :JohLenX))
