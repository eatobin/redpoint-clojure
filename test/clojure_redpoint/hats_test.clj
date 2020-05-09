(ns clojure-redpoint.hats-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.hats :as hat]
            [clojure-redpoint.players]
            [clojure.spec.alpha :as s]))

(def test-hat #{:PauMcc :GeoHar :JohLen :RinSta})
(def players {:PauMcc {:player-name  "Paul McCartney",
                       :gift-history [{:giver :JohLen, :givee :GeoHar}]},
              :GeoHar {:player-name  "George Harrison",
                       :gift-history [{:giver :PauMcc, :givee :RinSta}]},
              :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
              :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(deftest make-hats-test
  (is (= test-hat
         (hat/make-hat players))))

(deftest remove-puck-test
  (is (= #{:PauMcc :GeoHar :JohLen}
         (hat/remove-puck test-hat :RinSta))))

(deftest remove-puck-empty-test
  (is (= #{}
         (hat/remove-puck #{} :RinSta))))

(deftest discard-puck-givee-test
  (is (= #{:PauMcc :JohLen}
         (hat/discard-givee #{:PauMcc} :JohLen))))
(s/conform ::hat/discards
           (hat/discard-givee #{:PauMcc} :JohLen))

(deftest return-discards-test
  (is (= #{:PauMcc :JohLen :GeoHar}
         (hat/return-discards #{:PauMcc :JohLen} #{:GeoHar}))))

(deftest empty-discards-test
  (is (= #{}
         (hat/empty-discards))))
(s/conform ::hat/discards
           (hat/empty-discards))
