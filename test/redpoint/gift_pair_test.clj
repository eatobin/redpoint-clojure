(ns redpoint.gift-pair-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :as t]
            [redpoint.gift-pair :as gp]))

(def json-string "{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}")
(def bad-json-string "{\"givee\"\"GeoHar\",\"giver\":\"JohLen\"}")
(def bad-json-string-2 "{\"giveeX\":\"GeoHar\",\"giver\":\"JohLen\"}")


(def gift-pair {:givee :GeoHar :giver :JohLen})

(t/deftest gift-pair-json-string-to-Gift-Pair-test
  (t/is (= gift-pair
           (gp/gift-pair-json-string-to-Gift-Pair json-string)))
  (t/is (thrown? Exception
                 (gp/gift-pair-json-string-to-Gift-Pair bad-json-string)))
  (t/is (thrown? Exception
                 (gp/gift-pair-json-string-to-Gift-Pair bad-json-string-2))))

(s/conform ::gp/givee
           (:givee gift-pair))
(s/conform ::gp/giver
           (:giver gift-pair))

(t/deftest gift-pair-update-givee-test
  (t/is (= {:givee :NewBee :giver :JohLen}
           (gp/gift-pair-update-givee :NewBee gift-pair))))

(t/deftest gift-pair-update-giver-test
  (t/is (= {:givee :GeoHar :giver :NewBee}
           (gp/gift-pair-update-giver :NewBee gift-pair))))

(s/conform :unq/gift-pair
           (gp/gift-pair-update-givee :NewBee gift-pair))
(s/conform :unq/gift-pair
           (gp/gift-pair-update-giver :NewBee gift-pair))
