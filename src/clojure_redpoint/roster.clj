(ns clojure-redpoint.roster
  (:require [clojure-redpoint.players :as plrs]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::roster-name string?)
(s/def ::roster-year int?)
(s/def ::roster (s/keys :req-un [::roster-name ::roster-year ::plrs/players]))

(ostest/instrument)
