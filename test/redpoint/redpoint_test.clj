(ns redpoint.redpoint-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]
            [redpoint.domain :as dom]
            [redpoint.hats :as hat]
            [redpoint.players :as plrs]
            [redpoint.redpoint :as red]))

(def test-hat #{:PauMcc :GeoHar :JohLen :RinSta})
(def players {:PauMcc {:player-name  "Paul McCartney",
                       :gift-history [{:giver :JohLen, :givee :GeoHar}]},
              :GeoHar {:player-name  "George Harrison",
                       :gift-history [{:giver :PauMcc, :givee :RinSta}]},
              :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
              :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})
(def file-path "resources-test/beatles.json")
(def bad-file-path "nope.json")
(def bad-json-file "resources-test/bad-json.json")

(deftest roster-or-quit-success-test
  (red/roster-or-quit file-path)
  (is (= "The Beatles"
         (deref red/a-roster-name)))
  (is (= 2014
         (deref red/a-roster-year)))
  (is (= players
         (deref red/a-players))))

(deftest roster-or-quit-bad-path-test
  (is (nil? (red/roster-or-quit bad-file-path))))

(deftest roster-or-quit-bad-json-test
  (is (nil? (red/roster-or-quit bad-json-file))))

(s/conform ::dom/roster-name
           (do
             (red/roster-or-quit "resources-test/beatles.json")
             (deref red/a-roster-name)))
(s/conform ::dom/roster-year
           (do
             (red/roster-or-quit "resources-test/beatles.json")
             (deref red/a-roster-year)))
(s/conform :unq/players
           (do
             (red/roster-or-quit "resources-test/beatles.json")
             (deref red/a-players)))

(deftest draw-puck-test
  (is (true?
        (nil? (red/draw-puck #{}))))
  (is (true?
        (some? (red/draw-puck test-hat)))))
(s/conform ::dom/givee
           (red/draw-puck test-hat))
(s/conform nil?
           (red/draw-puck #{}))

(deftest start-new-year-test
  (reset! red/a-g-year 0)
  (reset! red/a-giver nil)
  (reset! red/a-givee nil)
  (red/roster-or-quit "resources-test/beatles.json")
  (red/start-new-year)
  (is (= 1
         (deref red/a-g-year)))
  (is (some?
        (deref red/a-giver)))
  (is (some?
        (deref red/a-givee)))
  (is (= {:player-name  "Ringo Starr",
          :gift-history [{:givee :JohLen, :giver :GeoHar}
                         {:givee :RinSta, :giver :RinSta}]}
         (get-in (deref red/a-players) [:RinSta])))
  (is (empty? (deref red/a-discards))))

(deftest select-new-giver-test
  (reset! red/a-g-year 0)
  (reset! red/a-giver nil)
  (reset! red/a-givee nil)
  (red/roster-or-quit "resources-test/beatles.json")
  (red/start-new-year)
  (swap! red/a-discards hat/discard-givee :GeoHar)
  (is (= 1
         (count (deref red/a-discards))))
  (red/select-new-giver)
  (is (= 3
         (count (deref red/a-gr-hat))))
  (is (= 0
         (count (deref red/a-discards)))))

(deftest givee-is-success-test
  (reset! red/a-g-year 0)
  (reset! red/a-giver nil)
  (reset! red/a-givee nil)
  (red/roster-or-quit "resources-test/beatles.json")
  (red/start-new-year)
  (let [temp-ge (deref red/a-givee)]
    (red/givee-is-success)
    (is (= temp-ge
           (plrs/players-get-givee (deref red/a-players) (deref red/a-giver) (deref red/a-g-year))))
    (is (= (deref red/a-giver)
           (plrs/players-get-giver (deref red/a-players) temp-ge (deref red/a-g-year))))
    (is (= nil
           (some #{temp-ge} (deref red/a-ge-hat))))))

(deftest givee-is-failure-test
  (reset! red/a-g-year 0)
  (reset! red/a-giver nil)
  (reset! red/a-givee nil)
  (red/roster-or-quit "resources-test/beatles.json")
  (red/start-new-year)
  (let [temp-ge (deref red/a-givee)]
    (red/givee-is-failure)
    (is (= temp-ge
           (some #{temp-ge} (deref red/a-discards))))
    (is (= nil
           (some #{temp-ge} (deref red/a-ge-hat))))))

(deftest errors?-test
  (reset! red/a-g-year 0)
  (reset! red/a-players {:RinSta {:player-name "Ringo Starr", :gift-history [{:givee :JohLen, :giver :GeoHar}]},
                         :JohLen {:player-name "John Lennon", :gift-history [{:givee :PauMcc, :giver :RinSta}]},
                         :GeoHar {:player-name "George Harrison", :gift-history [{:givee :GeoHar, :giver :whoops}]},
                         :PauMcc {:player-name "Paul McCartney", :gift-history [{:givee :yikes, :giver :PauMcc}]}})
  (is (= (seq [:GeoHar :PauMcc])
         (red/errors?))))
