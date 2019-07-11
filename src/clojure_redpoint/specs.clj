(ns clojure-redpoint.specs
  (:require [clojure-redpoint.roster-string-check :as rs]
            [clojure-redpoint.roster :as r]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as sgen]
            [clojure.test.check.generators :as gen]))

(def raw-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(def short-raw-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nPauMcc, Paul McCartney, GeoHar, JohLen\n")

(sgen/generate (s/gen int?))

(gen/sample gen/int)
(gen/sample (gen/list gen/boolean))
(gen/sample (gen/tuple gen/nat gen/boolean gen/ratio))

(stest/check `rs/scrub)
(s/exercise-fn `rs/scrub)
(s/explain ::rs/scrubbed (rs/scrub raw-string))
(s/conform ::rs/scrubbed
           (rs/scrub raw-string))
(s/conform (s/or :no-error (s/tuple ::rs/scrubbed nil?)
                 :error (s/tuple nil? string?))
           (rs/valid-length-string (rs/scrub raw-string)))
(s/explain (s/or :no-error (s/tuple ::rs/scrubbed nil?)
                 :error (s/tuple nil? string?))
           (rs/valid-length-string (rs/scrub raw-string)))
(s/conform (s/or :no-error (s/tuple ::rs/scrubbed nil?)
                 :error (s/tuple nil? string?))
           (rs/valid-length-string (rs/scrub short-raw-string)))
(s/conform (s/or :no-error (s/tuple ::rs/scrubbed nil?)
                 :error (s/tuple nil? string?))
           (rs/scrubbed-roster-string (rs/scrub short-raw-string)))
(s/conform (s/or :no-error (s/tuple ::rs/scrubbed nil?)
                 :error (s/tuple nil? string?))
           (rs/scrubbed-roster-string (rs/scrub raw-string)))

(s/conform vector?
           (rs/make-player-vectors (rs/scrub raw-string)))
(s/conform :unq/gift-pair
           (r/make-gift-pair "one" "two"))
(def x (r/make-gift-pair "joe" "bob"))
(def y (r/make-gift-pair "joey" "bobby"))
(def h [x y])
(s/conform :unq/gift-history h)
(s/conform :unq/player
           (r/make-player "eric" h))
(s/conform ::dom/plr-map (r/make-player-map ["s" "n" "ge" "gr"]))
(def pm (r/make-players-map (r/make-players-vector (rs/scrub raw-string))))
(s/conform (s/or :found :unq/player
                 :not-found nil?)
           (r/get-player-in-roster pm :GeoHar))
;; =>
;; [:found
;;  {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}]
(s/conform (s/or :found :unq/player
                 :not-found nil?)
           (r/get-player-in-roster pm :GeoHarX))
;; => [:not-found nil]
(s/conform (s/or :found ::dom/givee
                 :not-found nil?)
           (r/get-givee-in-gift-pair {:giver :PauMcc, :givee :RinSta}))
(s/conform :unq/player
           (r/set-gift-history-in-player [{:giver :RinSta, :givee :PauMcc}] {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}))
;(stest/check `r/set-gift-pair-in-gift-history)
