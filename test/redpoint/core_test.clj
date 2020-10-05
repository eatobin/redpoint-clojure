(ns redpoint.core-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.core :as core]
            [redpoint.domain :as dom]
            [redpoint.gift-pair :as gp]
            [redpoint.player :as plr]
            [redpoint.players :as plrs]
            [redpoint.hats :as hat]
            [clojure.spec.alpha :as s]))

(def test-hat #{:PauMcc :GeoHar :JohLen :RinSta})

(deftest roster-or-quit-test
  (core/roster-or-quit "resources-test/beatles.json")
  (is (= "The Beatles"
         (deref core/a-roster-name)))
  (is (= 2014
         (deref core/a-roster-year)))
  (is (= {:GeoHar (plr/map->Player {:gift-history [(gp/map->Gift-Pair {:givee :RinSta
                                                                       :giver :PauMcc})]
                                    :player-name  "George Harrison"})
          :JohLen (plr/map->Player {:gift-history [(gp/map->Gift-Pair {:givee :PauMcc
                                                                       :giver :RinSta})]
                                    :player-name  "John Lennon"})
          :PauMcc (plr/map->Player {:gift-history [(gp/map->Gift-Pair {:givee :GeoHar
                                                                       :giver :JohLen})]
                                    :player-name  "Paul McCartney"})
          :RinSta (plr/map->Player {:gift-history [(gp/map->Gift-Pair {:givee :JohLen
                                                                       :giver :GeoHar})]
                                    :player-name  "Ringo Starr"})}
         (deref core/a-players))))
(s/conform ::dom/roster-name
           (do
             (core/roster-or-quit "resources-test/beatles.json")
             (deref core/a-roster-name)))
(s/conform ::dom/roster-year
           (do
             (core/roster-or-quit "resources-test/beatles.json")
             (deref core/a-roster-year)))
(s/conform ::dom/players
           (do
             (core/roster-or-quit "resources-test/beatles.json")
             (deref core/a-players)))

(deftest draw-puck-test
  (is (true?
        (nil? (core/draw-puck #{}))))
  (is (true?
        (some? (core/draw-puck test-hat)))))
(s/conform ::dom/givee
           (core/draw-puck test-hat))
(s/conform nil?
           (core/draw-puck #{}))

(deftest start-new-year-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (core/roster-or-quit "resources-test/beatles.json")
  (core/start-new-year)
  (is (= 1
         (deref core/a-g-year)))
  (is (some?
        (deref core/a-giver)))
  (is (some?
        (deref core/a-givee)))
  (is (= (plr/map->Player {:player-name  "Ringo Starr",
                           :gift-history [(gp/map->Gift-Pair {:givee :JohLen, :giver :GeoHar})
                                          (gp/map->Gift-Pair {:givee :RinSta, :giver :RinSta})]})
         (get-in (deref core/a-players) [:RinSta])))
  (is (empty? (deref core/a-discards))))

(deftest select-new-giver-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (core/roster-or-quit "resources-test/beatles.json")
  (core/start-new-year)
  (swap! core/a-discards hat/discard-givee :GeoHar)
  (is (= 1
         (count (deref core/a-discards))))
  (core/select-new-giver)
  (is (= 3
         (count (deref core/a-gr-hat))))
  (is (= 0
         (count (deref core/a-discards)))))

(deftest givee-is-success-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (core/roster-or-quit "resources-test/beatles.json")
  (core/start-new-year)
  (let [temp-ge (deref core/a-givee)]
    (core/givee-is-success)
    (is (= temp-ge
           (plrs/players-get-givee (deref core/a-players) (deref core/a-giver) (deref core/a-g-year))))
    (is (= (deref core/a-giver)
           (plrs/players-get-giver (deref core/a-players) temp-ge (deref core/a-g-year))))
    (is (= nil
           (some #{temp-ge} (deref core/a-ge-hat))))))

(deftest givee-is-failure-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (core/roster-or-quit "resources-test/beatles.json")
  (core/start-new-year)
  (let [temp-ge (deref core/a-givee)]
    (core/givee-is-failure)
    (is (= temp-ge
           (some #{temp-ge} (deref core/a-discards))))
    (is (= nil
           (some #{temp-ge} (deref core/a-ge-hat))))))

(deftest errors?-test
  (reset! core/a-g-year 0)
  (reset! core/a-players {:RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [(gp/map->Gift-Pair {:givee :JohLen, :giver :GeoHar})]}),
                          :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [(gp/map->Gift-Pair {:givee :PauMcc, :giver :RinSta})]}),
                          :GeoHar (plr/map->Player {:player-name "George Harrison", :gift-history [(gp/map->Gift-Pair {:givee :GeoHar, :giver :PauMcc})]}),
                          :PauMcc (plr/map->Player {:player-name "Paul McCartney", :gift-history [(gp/map->Gift-Pair {:givee :GeoHar, :giver :PauMcc})]})})
  (is (= (seq [:GeoHar :PauMcc])
         (core/errors?))))
