(ns clojure-redpoint.gift-history-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.gift-history :as gh]
            [clojure.spec.alpha :as s]))

(def gift-history [{:giver :JohLen, :givee :GeoHar}])

;; (deftest add-year-test
;;   (is (= [{:giver :JohLen, :givee :GeoHar}, {:giver :NewBee, :givee :NewBee}]
;;          (gh/add-year gift-history :NewBee))))
;; (s/conform :unq/gift-history
;;            (gh/add-year gift-history :NewBee))

;; (deftest get-gift-pair-test
;;   (is (= {:giver :JohLen, :givee :GeoHar}
;;          (gh/get-gift-pair gift-history 0))))
;; (s/conform :unq/gift-pair
;;            (gh/get-gift-pair gift-history 0))

;; (deftest set-gift-pair-test
;;   (is (= [{:givee :me, :giver :you}]
;;          (gh/set-gift-pair gift-history 0 {:givee :me, :giver :you}))))
;; (s/conform :unq/gift-history
;;            (gh/set-gift-pair gift-history 0 {:givee :me, :giver :you}))
