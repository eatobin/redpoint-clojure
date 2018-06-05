(ns clojure-redpoint.specs)

(in-ns 'clojure-redpoint.roster)

(def rs "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")

(stest/check `extract-roster-info-vector)
(stest/check `extract-players-list)
(s/conform vector?
           (extract-roster-info-vector rs))
(s/conform nil?
           (extract-roster-info-vector ""))
(s/conform ::plrs-list
           (extract-players-list rs))
(s/conform ::plrs-list
           (extract-players-list ""))
