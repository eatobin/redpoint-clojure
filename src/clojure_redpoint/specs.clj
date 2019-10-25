;(ns clojure-redpoint.specs
;  (:require [clojure-redpoint.roster-string-check :as rsc]
;            [clojure-redpoint.roster :as ros]
;            [clojure.spec.alpha :as s]
;            [clojure.spec.test.alpha :as stest]
;            [clojure.spec.gen.alpha :as sgen]
;            [clojure.test.check.generators :as gen]))
;
;(def raw-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;(def short-raw-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
;
;(sgen/generate (s/gen int?))
;
;(gen/sample gen/int)
;(gen/sample (gen/list gen/boolean))
;(gen/sample (gen/tuple gen/nat gen/boolean gen/ratio))
;
;(stest/check `rsc/scrub)
;(s/exercise-fn `rsc/scrub)
;(s/explain ::rsc/scrubbed (rsc/scrub raw-string))
;(s/conform ::rsc/scrubbed
;           (rsc/scrub raw-string))
;(s/conform (s/or :no-error (s/tuple ::rsc/scrubbed nil?)
;                 :error (s/tuple nil? string?))
;           (rsc/valid-length-string (rsc/scrub raw-string)))
;(s/explain (s/or :no-error (s/tuple ::rsc/scrubbed nil?)
;                 :error (s/tuple nil? string?))
;           (rsc/valid-length-string (rsc/scrub raw-string)))
;(s/conform (s/or :no-error (s/tuple ::rsc/scrubbed nil?)
;                 :error (s/tuple nil? string?))
;           (rsc/valid-length-string (rsc/scrub short-raw-string)))
;(s/conform (s/or :no-error (s/tuple ::rsc/scrubbed nil?)
;                 :error (s/tuple nil? string?))
;           (rsc/scrubbed-roster-string (rsc/scrub short-raw-string)))
;(s/conform (s/or :no-error (s/tuple ::rsc/scrubbed nil?)
;                 :error (s/tuple nil? string?))
;           (rsc/scrubbed-roster-string (rsc/scrub raw-string)))
;
;(s/conform vector?
;           (rsc/make-player-vectors (rsc/scrub raw-string)))
;(s/conform :unq/gift-pair
;           (ros/make-gift-pair "one" "two"))
;(def x (ros/make-gift-pair "joe" "bob"))
;(def y (ros/make-gift-pair "joey" "bobby"))
;(def h [x y])
;(s/conform :unq/gift-history h)
;(s/conform :unq/player
;           (ros/make-player "eric" h))
;(s/conform ::ros/plr-map (ros/make-player-map ["s" "n" "ge" "gr"]))
;(def pm (ros/make-players-map (ros/make-players-vector (rsc/scrub raw-string))))
;(s/conform (s/or :found :unq/player
;                 :not-found nil?)
;           (ros/get-player-in-roster pm :GeoHar))
;;; =>
;;; [:found
;;;  {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}]
;(s/conform (s/or :found :unq/player
;                 :not-found nil?)
;           (ros/get-player-in-roster pm :GeoHarX))
;;; => [:not-found nil]
;(s/conform (s/or :found ::ros/givee
;                 :not-found nil?)
;           (ros/get-givee-in-gift-pair {:giver :PauMcc, :givee :RinSta}))
;(s/conform :unq/player
;           (ros/set-gift-history-in-player [{:giver :RinSta, :givee :PauMcc}] {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}))
;;(stest/check `ros/set-gift-pair-in-gift-history)
