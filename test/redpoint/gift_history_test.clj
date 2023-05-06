(ns redpoint.gift-history-test
  (:require [clojure.test :as t]
            [redpoint.gift-history :as gh]))

(def json-string "[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]")
;(def bad-json-string "[{\"givee\"\"GeoHar\",\"giver\":\"JohLen\"}]")
;(def bad-json-string-2 "[{\"giveeX\":\"GeoHar\",\"giver\":\"JohLen\"}]")

(def gift-history [{:givee :GeoHar :giver :JohLen}])

(t/deftest gift-history-json-string-to-gift-history-test
  (t/is (= gift-history
           (gh/gift-history-json-string-to-gift-history json-string))))
;(t/is (thrown? Exception
;               (gp/gift-pair-json-string-to-Gift-Pair bad-json-string)))
;(t/is (thrown? Exception
;               (gp/gift-pair-json-string-to-Gift-Pair bad-json-string-2))))

;(deftest add-year-test
;  (is (= [{:givee :GeoHar :giver :JohLen}, {:givee :NewBee :giver :NewBee}]
;         (gh/gift-history-add-year gift-history :NewBee))))
;(s/conform :unq/gift-history
;           (gh/gift-history-add-year gift-history :NewBee))
;
;(deftest get-gift-pair-test
;  (is (= {:givee :GeoHar :giver :JohLen}
;         (gift-history 0))))
;(s/conform :unq/gift-pair
;           (gift-history 0))
;
;(deftest set-gift-pair-test
;  (is (= [{:givee :me :giver :you}]
;         (gh/gift-history-update-gift-history gift-history 0 {:givee :me :giver :you}))))
;(s/conform :unq/gift-history
;           (gh/gift-history-update-gift-history gift-history 0 {:givee :me :giver :you}))
;
;(s/conform :unq/gift-history
;           (gh/gift-history-json-string-to-Gift-History json-string-GH))
