(ns clojure-redpoint.specs
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure-redpoint.roster :as rost]))

(stest/check `rost/extract-roster-info-vector)
(stest/check `rost/extract-players-list)
(stest/check `rost/make-gift-pair)
(stest/check `rost/make-player)
(stest/check `make-player-map)
(stest/check `get-gift-history-in-player)
(stest/check `get-gift-pair-in-gift-history)
