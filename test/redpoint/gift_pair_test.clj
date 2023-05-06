(ns redpoint.gift-pair-test
  (:require [clojure.test :as t]
            [redpoint.gift-pair :as gp]))


(def json-string "{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}")
(def bad-json-string "{\"givee\"\"GeoHar\",\"giver\":\"JohLen\"}")
(def bad-json-string-2 "{\"giveeX\":\"GeoHar\",\"giver\":\"JohLen\"}")


(def gift-pair {:givee :GeoHar :giver :JohLen})

(t/deftest gift-pair-json-string-to-Gift-Pair-test
  (t/is (= gift-pair
           (gp/gift-pair-json-string-to-gift-pair json-string)))
  (t/is (thrown? Exception
                 (gp/gift-pair-json-string-to-gift-pair bad-json-string)))
  (t/is (thrown? Exception
                 (gp/gift-pair-json-string-to-gift-pair bad-json-string-2))))

(t/deftest gift-pair-update-givee-test
  (t/is (= {:givee :NewBee :giver :JohLen}
           (gp/gift-pair-update-givee :NewBee gift-pair))))

(t/deftest gift-pair-update-giver-test
  (t/is (= {:givee :GeoHar :giver :NewBee}
           (gp/gift-pair-update-giver :NewBee gift-pair))))
