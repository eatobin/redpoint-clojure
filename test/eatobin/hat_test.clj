(ns eatobin.hat-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]
            [eatobin.hat :refer [hat-discard-givee
                                 hat-make-hat
                                 hat-remove-puck
                                 hat-return-discards]]))

(def hat-test-hat #{:PauMcc :GeoHar :JohLen :RinSta})

(def hat-test-players {:PauMcc {:player-name  "Paul McCartney",
                                :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                       :GeoHar {:player-name  "George Harrison",
                                :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                       :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                       :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(s/conform :unq/hat
           #{:PauMcc :GeoHar :JohLen})

(s/conform :unq/hat
           #{})

(deftest hat-make-hat-test
  (is (= hat-test-hat
         (hat-make-hat hat-test-players))))
(s/conform :unq/hat
           (hat-make-hat hat-test-players))

(deftest hat-remove-puck-test
  (is (= #{:PauMcc :GeoHar :JohLen}
         (hat-remove-puck :RinSta hat-test-hat))))

(deftest hat-remove-puck-empty-test
  (is (= #{}
         (hat-remove-puck :RinSta #{}))))

(deftest discard-puck-givee-test
  (is (= #{:PauMcc :JohLen}
         (hat-discard-givee :JohLen #{:PauMcc}))))
(s/conform :unq/discards
           (hat-discard-givee :JohLen #{:PauMcc}))

(deftest hat-return-discards-test
  (is (= #{:PauMcc :JohLen :GeoHar}
         (hat-return-discards #{:GeoHar} #{:PauMcc :JohLen}))))
