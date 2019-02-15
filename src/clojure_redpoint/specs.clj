(ns clojure-redpoint.specs
  (:require [clojure-redpoint.domain :as dom]
            [clojure-redpoint.roster-string-check :as rs]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as sgen]
            [clojure.test.check.generators :as gen]))

(def raw-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")

(sgen/generate (s/gen int?))

(gen/sample gen/int)
(gen/sample (gen/list gen/boolean))
(gen/sample (gen/tuple gen/nat gen/boolean gen/ratio))

(stest/check `rs/scrub)
(s/exercise-fn `rs/scrub)
(s/explain ::dom/scrubbed raw-string)
(s/conform ::dom/scrubbed
           (rs/scrub raw-string))
