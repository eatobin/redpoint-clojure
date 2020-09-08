(ns clojure-redpoint.roster
  (:require [clojure-redpoint.domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defrecord Roster [roster-name roster-year players])
(s/fdef ->Roster
        :args (s/cat :roster-name ::dom/roster-name
                     :roster-year ::dom/roster-year
                     :players ::dom/players)
        :ret ::dom/roster)

(ostest/instrument)
