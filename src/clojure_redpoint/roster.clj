(ns clojure-redpoint.roster
  (:require [clojure-redpoint.domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::roster-name string?)
(s/def ::roster-year int?)
(s/def ::roster (s/keys :req-un [::roster-name ::roster-year ::dom/players]))

(ostest/instrument)
