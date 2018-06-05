(ns clojure-redpoint.specs
  (:require [clojure-redpoint.roster :as rost]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as gen]))

(gen/generate (s/gen int?))

(in-ns 'clojure-redpoint.roster)
(def rs "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")

(stest/check `extract-roster-info)
(stest/check `extract-players-list)
(s/conform vector?
           (extract-roster-info rs))
(s/conform nil?
           (extract-roster-info ""))
(s/conform ::plrs-list
           (extract-players-list rs))
(s/conform ::plrs-list
           (extract-players-list ""))
