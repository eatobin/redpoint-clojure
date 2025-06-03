(ns eatobin.my-state-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]
            [eatobin.domain :as dom]
            [eatobin.my-state :refer [my-state-draw-puck
                                      my-state-errors
                                      my-state-givee-is-failure
                                      my-state-givee-is-success
                                      my-state-json-string-to-my-state
                                      my-state-print-results
                                      my-state-select-new-giver
                                      my-state-start-new-year]]
            [eatobin.players :refer [players-get-my-givee
                                     players-get-my-giver]]))

(def beatles-json "{\"rosterName\":\"The Beatles\",\"rosterYear\":2014,\"players\":{\"PauMcc\":{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"playerName\":\"George Harrison\",\"giftHistory\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"playerName\":\"John Lennon\",\"giftHistory\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"playerName\":\"Ringo Starr\",\"giftHistory\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}},\"gift-year\":0,\"givee-hat\":[],\"giver-hat\":[],\"maybe-givee\":null,\"maybe-giver\":null,\"discards\":[],\"quit\":\"n\"}")
(def beatles-state-0 {:discards    #{},
                      :roster-year 2014,
                      :gift-year   0,
                      :maybe-givee nil,
                      :givee-hat   #{},
                      :maybe-giver nil,
                      :players     {:PauMcc {:player-name  "Paul McCartney",
                                             :gift-history [{:givee :GeoHar, :giver :JohLen}]},
                                    :GeoHar {:player-name  "George Harrison",
                                             :gift-history [{:givee :RinSta, :giver :PauMcc}]},
                                    :JohLen {:player-name  "John Lennon",
                                             :gift-history [{:givee :PauMcc, :giver :RinSta}]},
                                    :RinSta {:player-name  "Ringo Starr",
                                             :gift-history [{:givee :JohLen, :giver :GeoHar}]}},
                      :quit        "n",
                      :roster-name "The Beatles",
                      :giver-hat   #{}})

(def test-hat #{:RinSta})
(def fresh-hat #{:RinSta, :JohLen, :GeoHar, :PauMcc})
(def rin-sta-plus {:player-name  "Ringo Starr",
                   :gift-history [{:givee :JohLen, :giver :GeoHar}
                                  {:givee :RinSta, :giver :RinSta}]})
(def weird-state {:discards    #{},
                  :roster-year 2014,
                  :gift-year   0,
                  :maybe-givee nil,
                  :givee-hat   #{},
                  :maybe-giver nil,
                  :players     {:PauMcc {:player-name  "pauYikes",
                                         :gift-history [{:givee :GeoHar, :giver :PauMcc}]},
                                :GeoHar {:player-name  "geoWhoops",
                                         :gift-history [{:givee :GeoHar, :giver :PauMcc}]},
                                :JohLen {:player-name  "John Lennon",
                                         :gift-history [{:givee :PauMcc, :giver :RinSta}]},
                                :RinSta {:player-name  "Ringo Starr",
                                         :gift-history [{:givee :JohLen, :giver :GeoHar}]}},
                  :quit        "n",
                  :roster-name "The Beatles",
                  :giver-hat   #{}})

(deftest my-state-json-string-to-my-state-test
  (is (= beatles-state-0
         (my-state-json-string-to-my-state beatles-json))))
(s/conform :unq/my-state
           (my-state-json-string-to-my-state beatles-json))

(deftest my-state-draw-puck-not-nil-test
  (is (= :RinSta
         (my-state-draw-puck test-hat))))
(s/conform ::dom/maybe-player-key
           (my-state-draw-puck test-hat))

(deftest my-state-draw-puck-nil-test
  (is (= nil
         (my-state-draw-puck #{}))))
(s/conform ::dom/maybe-player-key
           (my-state-draw-puck #{}))

(deftest my-state-start-new-year-test
  (let [beatles-state-1 (my-state-start-new-year beatles-state-0)]
    (is (= 1
           (:gift-year beatles-state-1)))
    (is (= fresh-hat
           (:givee-hat beatles-state-1)))
    (is (= fresh-hat
           (:giver-hat beatles-state-1)))
    (is (not= nil
              (:maybe-giver beatles-state-1)))
    (is (not= nil
              (:maybe-givee beatles-state-1)))
    (is (= rin-sta-plus
           (get-in beatles-state-1 [:players :RinSta])))
    (is (= #{}
           (:discards beatles-state-1)))))

(deftest my-state-givee-is-failure-test
  (let [beatles-state-1 (my-state-start-new-year beatles-state-0)
        bad-givee (:maybe-givee beatles-state-1)
        beatles-state-2 (my-state-givee-is-failure beatles-state-1)]
    (is (not (contains? (:givee-hat beatles-state-2) bad-givee)))
    (is (not= bad-givee
              (:maybe-givee beatles-state-2)))
    (is (contains? (:discards beatles-state-2) bad-givee))))

(deftest my-state-givee-is-success-test
  (let [beatles-state-1 (my-state-start-new-year beatles-state-0)
        good-givee (:maybe-givee beatles-state-1)
        good-giver (:maybe-giver beatles-state-1)
        beatles-state-2 (my-state-givee-is-success beatles-state-1)]
    (is (= good-givee
           (players-get-my-givee good-giver (:players beatles-state-2) (:gift-year beatles-state-2))))
    (is (= good-giver
           (players-get-my-giver good-givee (:players beatles-state-2) (:gift-year beatles-state-2))))
    (is (not (contains? (:givee-hat beatles-state-2) good-givee)))
    (is (= nil
           (:maybe-givee beatles-state-2)))))

(deftest my-state-select-new-giver-test
  (let [beatles-state-1 (my-state-start-new-year beatles-state-0)
        bad-givee (:maybe-givee beatles-state-1)
        beatles-state-2 (my-state-givee-is-failure beatles-state-1)
        good-givee (:maybe-givee beatles-state-2)
        good-giver (:maybe-giver beatles-state-2)
        beatles-state-3 (my-state-givee-is-success beatles-state-2)
        beatles-state-4 (my-state-select-new-giver beatles-state-3)]
    (is (contains? (:givee-hat beatles-state-4) bad-givee))
    (is (not (contains? (:givee-hat beatles-state-4) good-givee)))
    (is (not (contains? (:giver-hat beatles-state-4) good-giver)))
    (is (not= good-givee
              (:maybe-givee beatles-state-4)))
    (is (not= good-giver
              (:maybe-giver beatles-state-4)))
    (is (= #{}
           (:discards beatles-state-4)))))

(deftest my-state-errors-test
  (is (= '(:GeoHar :PauMcc)
         (my-state-errors weird-state))))

(deftest my-state-print-results-test
  (is (= beatles-state-0
         (my-state-print-results beatles-state-0)))
  (is (= weird-state
         (my-state-print-results weird-state))))
