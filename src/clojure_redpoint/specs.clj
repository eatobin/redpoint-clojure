; Don't load this file. Instead copy ns + require to repl and eval checks

(ns clojure-redpoint.specs
  (:require [clojure.spec.test.alpha :as stest]
            [clojure-redpoint.roster :as rost]))

(stest/check `rost/extract-roster-info-vector)
(stest/check `rost/extract-players-list)
(stest/check `rost/make-gift-pair)
(stest/check `rost/make-player)
(stest/check `rost/make-player-map)
(stest/check `rost/get-gift-history-in-player)
(stest/check `rost/get-gift-pair-in-gift-history)
(stest/check `rost/get-givee-in-gift-pair)
(stest/check `rost/set-gift-pair-in-gift-history)
(stest/check `rost/set-gift-history-in-player)
(stest/check `rost/set-gift-pair-in-roster)
