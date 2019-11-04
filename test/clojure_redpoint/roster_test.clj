(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster :as ros]))

(def roster {:roster-name "The Beatles",
             :roster-year 2014,
             :players     {:PauMcc {:name         "Paul McCartney",
                                    :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                           :GeoHar {:name         "George Harrison",
                                    :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                           :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                           :RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}})

(deftest get-roster-name-test
  (is (= "The Beatles"
         (ros/get-roster-name roster))))

(deftest get-roster-year-test
  (is (= 2014
         (ros/get-roster-year roster))))

;(deftest make-players-vector-test
;  (is (= players-vector
;         (ros/make-players-vector scrubbed))))
;(s/conform ::ros/plrs-vector
;           (ros/make-players-vector scrubbed))
;
;(deftest make-gift-pair-test
;  (is (= {:givee :PauMcc, :giver :GeoHar}
;         (ros/make-gift-pair "PauMcc" "GeoHar"))))
;(s/conform :unq/gift-pair
;           (ros/make-gift-pair "me" "you"))
;
;(deftest make-player-test
;  (is (= player
;         (ros/make-player "Ringo Starr" [{:giver :GeoHar, :givee :JohLen}]))))
;(s/conform :unq/player
;           (ros/make-player "us" [{:giver :me, :givee :meToo} {:givee :you :giver :youToo}]))
;
;(deftest make-player-map-test
;  (is (= {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
;         (ros/make-player-map ["RinSta" "Ringo Starr" "JohLen" "GeoHar"]))))
;(s/conform ::ros/plr-map
;           (ros/make-player-map ["s" "n" "ge" "gr"]))
;
;(deftest make-players-map-test
;  (is (= players-map
;         (ros/make-players-map players-vector))))
;(s/conform ::ros/plr-map
;           (ros/make-players-map players-vector))
;
;(deftest get-player-in-roster-test
;  (is (= {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}
;         (ros/get-player-in-roster players-map :GeoHar))))
;(s/conform :unq/player
;           (ros/get-player-in-roster {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                                      :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                                      :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
;                                      :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}
;                                     :PauMcc))
;
;(deftest get-gift-history-in-player-test
;  (is (= [{:giver :JohLen, :givee :GeoHar}]
;         (ros/get-gift-history-in-player {:name         "Paul McCartney",
;                                          :gift-history [{:giver :JohLen, :givee :GeoHar}]}))))
;(s/conform :unq/gift-history
;           (ros/get-gift-history-in-player {:name         "Paul McCartney",
;                                            :gift-history [{:giver :JohLen, :givee :GeoHar}]}))
;
;(deftest get-gift-pair-in-gift-history-test
;  (is (= {:giver :JohLen, :givee :GeoHar}
;         (ros/get-gift-pair-in-gift-history [{:giver :JohLen, :givee :GeoHar}] 0))))
;(s/conform :unq/gift-pair
;           (ros/get-gift-pair-in-gift-history [{:giver :GeoHar, :givee :JohLen}] 0))
;
;(deftest get-gift-pair-in-roster-test
;  (is (= {:giver :JohLen :givee :GeoHar}
;         (ros/get-gift-pair-in-roster players-map :PauMcc 0))))
;(s/conform :unq/gift-pair
;           (ros/get-gift-pair-in-roster players-map :PauMcc 0))
;
;(deftest get-givee-in-gift-pair-test
;  (is (= :GeoHar
;         (ros/get-givee-in-gift-pair {:giver :JohLen :givee :GeoHar}))))
;(s/conform ::ros/givee
;           (ros/get-givee-in-gift-pair {:giver :JohLen :givee :GeoHar}))
;
;(deftest get-giver-in-gift-pair-test
;  (is (= :JohLen
;         (ros/get-giver-in-gift-pair {:giver :JohLen :givee :GeoHar}))))
;(s/conform ::ros/giver
;           (ros/get-giver-in-gift-pair {:giver :JohLen :givee :GeoHar}))
;
;(deftest set-gift-history-in-player-test
;  (is (= {:name "John Lennon", :gift-history [{:giver :RinStaX, :givee :PauMccX}]}
;         (ros/set-gift-history-in-player [{:giver :RinStaX, :givee :PauMccX}]
;                                         {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}))))
;(s/conform :unq/player
;           (ros/set-gift-history-in-player [{:giver :RinStaX, :givee :PauMccX}]
;                                           {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}))
;
;(deftest set-gift-pair-in-gift-history-test
;  (is (= [{:giver :me, :givee :you}]
;         (ros/set-gift-pair-in-gift-history [{:giver :RinStaX, :givee :PauMccX}]
;                                            0
;                                            {:giver :me, :givee :you}))))
;(s/conform :unq/gift-history
;           (ros/set-gift-pair-in-gift-history [{:giver :RinStaX, :givee :PauMccX}]
;                                              0
;                                              {:giver :me, :givee :you}))
;
;(deftest set-gift-pair-in-roster-test
;  (is (= {:PauMcc {:name         "Paul McCartney",
;                   :gift-history [{:giver :JohLen, :givee :GeoHar}]},
;          :GeoHar {:name "George Harrison", :gift-history [{:giver :me, :givee :you}]},
;          :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;          :RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}}
;         (ros/set-gift-pair-in-roster players-map :GeoHar 0 {:giver :me, :givee :you}))))
;(s/conform ::ros/plr-map
;           (ros/set-gift-pair-in-roster players-map :GeoHar 0 {:giver :me, :givee :you}))
;
;(deftest add-year-in-player-test
;  (is (= {:name         "Ringo Starr",
;          :gift-history [{:giver :GeoHar, :givee :JohLen} {:giver :RinSta, :givee :RinSta}]}
;         (ros/add-year-in-player {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]} :RinSta))))
;(s/conform :unq/player
;           (ros/add-year-in-player {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]} :RinSta))
;
;(deftest add-year-in-roster-test
;  (is (= players-map-add
;         (ros/add-year-in-roster players-map))))
;(s/conform ::ros/plr-map
;           (ros/add-year-in-roster players-map))
;
;(deftest get-player-name-in-roster-test
;  (is (= "Ringo Starr"
;         (ros/get-player-name-in-roster players-map :RinSta))))
;
;(deftest get-givee-in-roster-test
;  (is (= :GeoHar
;         (ros/get-givee-in-roster players-map :PauMcc 0))))
;(s/conform ::ros/givee
;           (ros/get-givee-in-roster players-map :RinSta 0))
;
;(deftest get-giver-in-roster-test
;  (is (= :JohLen
;         (ros/get-giver-in-roster players-map :PauMcc 0))))
;(s/conform ::ros/giver
;           (ros/get-giver-in-roster players-map :RinSta 0))
;;(s/conform ::ros/plr-map
;;           (set-giver-in-roster players-map :RinSta 0 :RinSta))
;
;(deftest set-givee-in-roster-test
;  (is (= players-map-ge
;         (ros/set-givee-in-roster players-map :GeoHar 0 :GeoHar))))
;
;(deftest set-giver-in-roster-test
;  (is (= players-map-gr
;         (ros/set-giver-in-roster players-map :GeoHar 0 :GeoHar))))
;
;(stest/check `ros/make-gift-pair)
