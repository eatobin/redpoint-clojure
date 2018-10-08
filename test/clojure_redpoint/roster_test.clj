(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster :refer :all]
            [clojure.spec.alpha :as s]
            [clojure-redpoint.domain :as dom]))

(def scrubbed "The Beatles,2014\nRinSta,Ringo Starr,JohLen,GeoHar\nJohLen,John Lennon,PauMcc,RinSta\nGeoHar,George Harrison,RinSta,PauMcc\nPauMcc,Paul McCartney,GeoHar,JohLen")

(def players-map {:PauMcc {:name         "Paul McCartney",
                           :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                  :GeoHar {:name         "George Harrison",
                           :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                  :RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(def players-vector [["RinSta" "Ringo Starr" "JohLen" "GeoHar"]
                     ["JohLen" "John Lennon" "PauMcc" "RinSta"]
                     ["GeoHar" "George Harrison" "RinSta" "PauMcc"]
                     ["PauMcc" "Paul McCartney" "GeoHar" "JohLen"]])

(def players-map-ge {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
                     :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                     :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :GeoHar}]},
                     :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})

(def players-map-gr {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
                     :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                     :GeoHar {:name "George Harrison", :gift-history [{:giver :GeoHar, :givee :RinSta}]},
                     :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})

(def players-map-add {:RinSta {:name         "Ringo Starr",
                               :gift-history [{:giver :GeoHar, :givee :JohLen}
                                              {:giver :none, :givee :none}]},
                      :JohLen {:name         "John Lennon",
                               :gift-history [{:giver :RinSta, :givee :PauMcc}
                                              {:giver :none, :givee :none}]},
                      :GeoHar {:name         "George Harrison",
                               :gift-history [{:giver :PauMcc, :givee :RinSta}
                                              {:giver :none, :givee :none}]},
                      :PauMcc {:name         "Paul McCartney",
                               :gift-history [{:giver :JohLen, :givee :GeoHar}
                                              {:giver :none, :givee :none}]}})

(def player {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]})

(deftest get-roster-name-test
  (is (= "The Beatles"
         (get-roster-name scrubbed))))

(deftest get-roster-year-test
  (is (= "2014"
         (get-roster-year scrubbed))))

(deftest make-players-vector-test
  (is (= players-vector
         (make-players-vector scrubbed))))
(s/conform ::dom/plrs-vector
           (make-players-vector scrubbed))

(deftest make-gift-pair-test
  (is (= {:givee :PauMcc, :giver :GeoHar}
         (make-gift-pair "PauMcc" "GeoHar"))))
(s/conform :unq/gift-pair
           (make-gift-pair "me" "you"))

(deftest make-player-test
  (is (= {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}
         (make-player "Ringo Starr" [{:giver :GeoHar, :givee :JohLen}]))))
(s/conform :unq/player
           (make-player "us" [{:giver :me, :givee :meToo} {:givee :you :giver :youToo}]))

(deftest make-player-map-test
  (is (= {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
         (make-player-map ["RinSta" "Ringo Starr" "JohLen" "GeoHar"]))))
(s/conform ::dom/plr-map
           (make-player-map ["s" "n" "ge" "gr"]))

(deftest make-players-map-test
  (is (= players-map
         (make-players-map players-vector))))
(s/conform ::dom/plr-map
           (make-players-map players-vector))

(deftest get-player-in-roster-test
  (is (= {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}
         (get-player-in-roster players-map :GeoHar))))
(s/conform :unq/player
           (get-player-in-roster {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
                                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                                  :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                                  :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}
                                 :PauMcc))

(deftest get-gift-history-in-player-test
  (is (= [{:giver :JohLen, :givee :GeoHar}]
         (get-gift-history-in-player {:name         "Paul McCartney",
                                      :gift-history [{:giver :JohLen, :givee :GeoHar}]}))))
(s/conform :unq/gift-history
           (get-gift-history-in-player {:name         "Paul McCartney",
                                        :gift-history [{:giver :JohLen, :givee :GeoHar}]}))

(deftest get-gift-pair-in-gift-history-test
  (is (= {:giver :JohLen, :givee :GeoHar}
         (get-gift-pair-in-gift-history [{:giver :JohLen, :givee :GeoHar}] 0))))
(s/conform :unq/gift-pair
           (get-gift-pair-in-gift-history [{:giver :GeoHar, :givee :JohLen}] 0))

(deftest get-gift-pair-in-roster-test
  (is (= {:giver :JohLen :givee :GeoHar}
         (get-gift-pair-in-roster players-map :PauMcc 0))))
(s/conform :unq/gift-pair
           (get-gift-pair-in-roster players-map :PauMcc 0))

(deftest get-givee-in-gift-pair-test
  (is (= :GeoHar
         (get-givee-in-gift-pair {:giver :JohLen :givee :GeoHar}))))
(s/conform ::dom/givee
           (get-givee-in-gift-pair {:giver :JohLen :givee :GeoHar}))

(deftest get-giver-in-gift-pair-test
  (is (= :JohLen
         (get-giver-in-gift-pair {:giver :JohLen :givee :GeoHar}))))
(s/conform ::dom/giver
           (get-giver-in-gift-pair {:giver :JohLen :givee :GeoHar}))

(deftest set-gift-history-in-player-test
  (is (= {:name "John Lennon", :gift-history [{:giver :RinStaX, :givee :PauMccX}]}
         (set-gift-history-in-player [{:giver :RinStaX, :givee :PauMccX}]
                                     {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}))))
(s/conform :unq/player
           (set-gift-history-in-player [{:giver :RinStaX, :givee :PauMccX}]
                                       {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}))

(deftest set-gift-pair-in-gift-history-test
  (is (= [{:giver :me, :givee :you}]
         (set-gift-pair-in-gift-history [{:giver :RinStaX, :givee :PauMccX}]
                                        0
                                        {:giver :me, :givee :you}))))
(s/conform :unq/gift-history
           (set-gift-pair-in-gift-history [{:giver :RinStaX, :givee :PauMccX}]
                                          0
                                          {:giver :me, :givee :you}))

(deftest set-gift-pair-in-roster-test
  (is (= {:PauMcc {:name         "Paul McCartney",
                   :gift-history [{:giver :JohLen, :givee :GeoHar}]},
          :GeoHar {:name "George Harrison", :gift-history [{:giver :me, :givee :you}]},
          :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
          :RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
         (set-gift-pair-in-roster players-map :GeoHar 0 {:giver :me, :givee :you}))))
(s/conform ::dom/plr-map
           (set-gift-pair-in-roster players-map :GeoHar 0 {:giver :me, :givee :you}))

(deftest check-give-test
  (is (true? (check-give players-map :GeoHar 0 :PauMcc)))
  (is (false? (check-give players-map :GeoHar 1 :PauMcc)))
  (is (false? (check-give players-map :GeoHar 0 :PauMccX))))

(deftest add-year-in-player-test
  (is (= {:name         "Ringo Starr",
          :gift-history [{:giver :GeoHar, :givee :JohLen} {:giver :none, :givee :none}]}
         (add-year-in-player {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}))))
(s/conform :unq/player
           (add-year-in-player {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}))

(deftest add-year-in-roster-test
  (is (= {:PauMcc {:name         "Paul McCartney",
                   :gift-history [{:giver :JohLen, :givee :GeoHar}
                                  {:giver :none, :givee :none}]},
          :GeoHar {:name         "George Harrison",
                   :gift-history [{:giver :PauMcc, :givee :RinSta}
                                  {:giver :none, :givee :none}]},
          :JohLen {:name         "John Lennon",
                   :gift-history [{:giver :RinSta, :givee :PauMcc}
                                  {:giver :none, :givee :none}]},
          :RinSta {:name         "Ringo Starr",
                   :gift-history [{:giver :GeoHar, :givee :JohLen}
                                  {:giver :none, :givee :none}]}}
         (add-year-in-roster players-map))))
(s/conform ::dom/plr-map
           (add-year-in-roster players-map))

(deftest get-player-name-in-roster-test
  (is (= "Ringo Starr"
         (get-player-name-in-roster players-map :RinSta))))

(deftest get-givee-in-roster-test
  (is (= :GeoHar
         (get-givee-in-roster players-map :PauMcc 0))))

(deftest get-giver-in-roster-test
  (is (= :JohLen
         (get-giver-in-roster players-map :PauMcc 0))))

(deftest set-givee-in-roster-pass-test
  (is (= players-map-ge
         (set-givee-in-roster players-map :GeoHar 0 :GeoHar))))

(deftest set-givee-in-roster-fail-plr-test
  (is (= players-map
         (set-givee-in-roster players-map :GeoHarX 0 :GeoHar))))
;;
;;(deftest set-givee-in-roster-fail-yr-test
;;  (is (= players-map
;;         (set-givee-in-roster players-map :GeoHar 9 :GeoHar))))
;;
;;(deftest set-givee-in-roster-fail-ge-test
;;  (is (= players-map
;;         (set-givee-in-roster players-map :GeoHar 0 :GeoHarX))))
;;
;;(deftest set-giver-in-roster-pass-test
;;  (is (= players-map-gr
;;         (set-giver-in-roster players-map :GeoHar 0 :GeoHar))))
;;
;;(deftest set-giver-in-roster-fail-plr-test
;;  (is (= players-map
;;         (set-giver-in-roster players-map :GeoHarX 0 :GeoHar))))
;;
;;(deftest set-giver-in-roster-fail-yr-test
;;  (is (= players-map
;;         (set-giver-in-roster players-map :GeoHar 9 :GeoHar))))
;;
;;(deftest set-giver-in-roster-fail-ge-test
;;  (is (= players-map
;;         (set-giver-in-roster players-map :GeoHar 0 :GeoHarX))))
;;
;;(deftest add-year-in-roster-test
;;  (is (= players-map-add
;;         (add-year-in-roster players-map))))
