(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster-utility :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure-redpoint.domain :as dom]))

(def roster-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(def test-roster-seq (lazy-seq '(["The Beatles" "2014"]
                                  ["RinSta" "Ringo Starr" "JohLen" "GeoHar"]
                                  ["JohLen" "John Lennon" "PauMcc" "RinSta"]
                                  ["GeoHar" "George Harrison" "RinSta" "PauMcc"]
                                  ["PauMcc" "Paul McCartney" "GeoHar" "JohLen"])))
(def players-map {:PauMcc {:name         "Paul McCartney",
                           :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                  :GeoHar {:name         "George Harrison",
                           :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                  :RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})
(deftest make-roster-seq-test
  (is (nil? (make-roster-seq "")))
  (is (= test-roster-seq
         (make-roster-seq roster-string))))
(s/conform ::dom/roster-seq
           (make-roster-seq roster-string))
(s/conform nil?
           (make-roster-seq ""))
;(stest/check `make-roster-seq)

(deftest extract-roster-info-vector-test
  (is (nil? (extract-roster-info-vector nil)))
  (is (= ["The Beatles" "2014"]
         (extract-roster-info-vector test-roster-seq))))
(s/conform ::dom/roster-line
           (extract-roster-info-vector test-roster-seq))
(s/conform nil?
           (extract-roster-info-vector nil))
;(stest/check `extract-roster-info-vector)

(deftest extract-players-list-test
  (is (nil? (extract-players-list nil)))
  (is (= '(["PauMcc" "Paul McCartney" "GeoHar" "JohLen"]
            ["GeoHar" "George Harrison" "RinSta" "PauMcc"]
            ["JohLen" "John Lennon" "PauMcc" "RinSta"]
            ["RinSta" "Ringo Starr" "JohLen" "GeoHar"])
         (extract-players-list test-roster-seq))))
(s/conform ::dom/plrs-list
           (extract-players-list test-roster-seq))
(s/conform nil?
           (extract-players-list nil))
(stest/check `extract-players-list)

(deftest make-gift-pair-test
  (is (= {:givee :PauMcc, :giver :GeoHar}
         (make-gift-pair "PauMcc" "GeoHar"))))
(s/conform :unq/gift-pair
           (make-gift-pair "me" "you"))
;(stest/check `make-gift-pair)
;
;(deftest make-player-test
;  (is (= {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}
;         (make-player "Ringo Starr" [{:giver :GeoHar, :givee :JohLen}]))))
;(s/conform :unq/player
;           (make-player "us" [{:giver :me, :givee :meToo} {:givee :you :giver :youToo}]))
;(stest/check `make-player)
;
;(deftest make-player-map-test
;  (is (= {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
;         (make-player-map ["RinSta" "Ringo Starr" "JohLen" "GeoHar"]))))
;(s/conform ::dom/plr-map
;           (make-player-map ["s" "n" "ge" "gr"]))
;;; (stest/check `make-player-map)
;
;(deftest make-players-map-test
;  (is (= players-map
;         (make-players-map roster-string))))
;(s/conform ::dom/plr-map
;           (make-players-map roster-string))
;;; (stest/check `make-players-map)
;
;(def test-players-map {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                       :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                       :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
;                       :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})
;
;(deftest get-player-in-roster-test
;  (is (= {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}
;         (get-player-in-roster test-players-map :GeoHar))))
;(s/conform (s/or :found :unq/player
;                 :not-found nil?)
;           (get-player-in-roster {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                                  :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
;                                  :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}
;                                 :PauMcc))
;(s/conform (s/or :found :unq/player
;                 :not-found nil?)
;           (get-player-in-roster {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                                  :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
;                                  :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}
;                                 :PauMccX))
;;; (stest/check `get-player-in-roster) ****memory failure****
;
;(deftest get-gift-history-in-player-test
;  (is (= [{:giver :JohLen, :givee :GeoHar}]
;         (get-gift-history-in-player {:name         "Paul McCartney",
;                                      :gift-history [{:giver :JohLen, :givee :GeoHar}]}))))
;
;(deftest get-gift-pair-in-gift-history-test
;  (is (= {:giver :JohLen, :givee :GeoHar}
;         (get-gift-pair-in-gift-history [{:giver :JohLen, :givee :GeoHar}] 0)))
;  (is (nil? (get-gift-pair-in-gift-history [{:giver :JohLen, :givee :GeoHar}] 1))))
;(s/conform (s/or :found :unq/gift-pair
;                 :not-found nil?)
;           (get-gift-pair-in-gift-history [{:giver :GeoHar, :givee :JohLen}] 0))
;(s/conform (s/or :found :unq/gift-pair
;                 :not-found nil?)
;           (get-gift-pair-in-gift-history [{:giver :GeoHar, :givee :JohLen}] 1))
;;; (stest/check `get-gift-pair-in-gift-history)
;
;(deftest get-gift-pair-in-roster-test
;  (is (= {:giver :JohLen :givee :GeoHar}
;         (get-gift-pair-in-roster players-map :PauMcc 0)))
;  (is (nil? (get-gift-pair-in-roster players-map :PauMcc 1))))
;(s/conform (s/or :found :unq/gift-pair
;                 :not-found nil?)
;           (get-gift-pair-in-roster players-map :PauMcc 0))
;(s/conform (s/or :found :unq/gift-pair
;                 :not-found nil?)
;           (get-gift-pair-in-roster players-map :PauMcc 1))
;(stest/check `get-gift-pair-in-roster)

;(def roster-info-vector (extract-roster-info-vector roster-string))

;(def player-list (extract-players-list roster-string))


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
