(ns clojure-redpoint.roster
  (:require [clojure.data.json :as json]
            [clojure-redpoint.domain :as dom]
            [clojure-redpoint.players :as plrs]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defrecord Roster [roster-name roster-year players])
(s/fdef ->Roster
        :args (s/cat :roster-name ::dom/roster-name
                     :roster-year ::dom/roster-year
                     :players ::dom/players)
        :ret ::dom/roster)

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn json-string-to-roster [json-string]
  (let [roster (json/read-str json-string
                              :value-fn my-value-reader
                              :key-fn keyword)]
    (->Roster (roster :roster-name)
              (roster :roster-year)
              (plrs/players-plain-player-upgrade (roster :players)))))

(ostest/instrument)
