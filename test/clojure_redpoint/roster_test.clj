(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster-utility :as rost-u]
            [clojure.spec.alpha :as s]))

(def roster-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(def test-roster-seq (lazy-seq (list ["The Beatles" "2014"]
                                     ["RinSta" "Ringo Starr" "JohLen" "GeoHar"]
                                     ["JohLen" "John Lennon" "PauMcc" "RinSta"]
                                     ["GeoHar" "George Harrison" "RinSta" "PauMcc"]
                                     ["PauMcc" "Paul McCartney" "GeoHar" "JohLen"])))
(deftest make-roster-seq
  (is (nil? (rost-u/make-roster-seq "")))
  (is (nil? (rost-u/make-roster-seq nil)))
  (is (= test-roster-seq
         (rost-u/make-roster-seq roster-string))))
(s/conform ::rost-u/roster-seq
           (rost-u/make-roster-seq roster-string))
(s/conform nil?
           (rost-u/make-roster-seq ""))
(s/conform nil?
           (rost-u/make-roster-seq nil))

;(def roster-info-vector (rost/extract-roster-info-vector roster-string))
;
;(def player-list (rost/extract-players-list roster-string))



;(def test-players-map {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                       :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                       :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
;                       :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})
;
;(def test-players-map-ge {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                          :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                          :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :GeoHar}]},
;                          :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})
;
;(def test-players-map-gr {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                          :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                          :GeoHar {:name "George Harrison", :gift-history [{:giver :GeoHar, :givee :RinSta}]},
;                          :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})
;
;(def test-players-map-add {:RinSta {:name         "Ringo Starr",
;                                    :gift-history [{:giver :GeoHar, :givee :JohLen}
;                                                   {:giver :none, :givee :none}]},
;                           :JohLen {:name         "John Lennon",
;                                    :gift-history [{:giver :RinSta, :givee :PauMcc}
;                                                   {:giver :none, :givee :none}]},
;                           :GeoHar {:name         "George Harrison",
;                                    :gift-history [{:giver :PauMcc, :givee :RinSta}
;                                                   {:giver :none, :givee :none}]},
;                           :PauMcc {:name         "Paul McCartney",
;                                    :gift-history [{:giver :JohLen, :givee :GeoHar}
;                                                   {:giver :none, :givee :none}]}})
;
;(def test-player {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]})
;
;(deftest get-roster-name-test
;  (is (= "The Beatles"
;         (get-roster-name test-roster-list))))
;
;(deftest get-roster-year-test
;  (is (= 2014
;         (get-roster-year test-roster-list))))
;
;(deftest make-players-map-test
;  (is (= test-players-map
;         (make-players-map test-roster-list))))
;
;(deftest get-player-in-roster-test
;  (is (= test-player
;         (get-player-in-roster test-players-map :RinSta))))
;
;(deftest get-player-name-in-roster-test
;  (is (= "Ringo Starr"
;         (get-player-name-in-roster test-players-map :RinSta))))
;
;(deftest get-givee-in-roster-test
;  (is (= :GeoHar
;         (get-givee-in-roster test-players-map :PauMcc 0))))
;
;(deftest set-givee-in-roster-pass-test
;  (is (= test-players-map-ge
;         (set-givee-in-roster test-players-map :GeoHar 0 :GeoHar))))
;
;(deftest set-givee-in-roster-fail-plr-test
;  (is (= test-players-map
;         (set-givee-in-roster test-players-map :GeoHarX 0 :GeoHar))))
;
;(deftest set-givee-in-roster-fail-yr-test
;  (is (= test-players-map
;         (set-givee-in-roster test-players-map :GeoHar 9 :GeoHar))))
;
;(deftest set-givee-in-roster-fail-ge-test
;  (is (= test-players-map
;         (set-givee-in-roster test-players-map :GeoHar 0 :GeoHarX))))
;
;(deftest set-giver-in-roster-pass-test
;  (is (= test-players-map-gr
;         (set-giver-in-roster test-players-map :GeoHar 0 :GeoHar))))
;
;(deftest set-giver-in-roster-fail-plr-test
;  (is (= test-players-map
;         (set-giver-in-roster test-players-map :GeoHarX 0 :GeoHar))))
;
;(deftest set-giver-in-roster-fail-yr-test
;  (is (= test-players-map
;         (set-giver-in-roster test-players-map :GeoHar 9 :GeoHar))))
;
;(deftest set-giver-in-roster-fail-ge-test
;  (is (= test-players-map
;         (set-giver-in-roster test-players-map :GeoHar 0 :GeoHarX))))
;
;(deftest add-year-in-roster-test
;  (is (= test-players-map-add
;         (add-year-in-roster test-players-map))))
