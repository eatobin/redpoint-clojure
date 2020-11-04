(ns redpoint.roster
  (:require [redpoint.domain]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(defn- my-key-reader
  [key]
  (cond
    (= key "playerName") :player-name
    (= key "giftHistory") :gift-history
    (= key "rosterName") :roster-name
    (= key "rosterYear") :roster-year
    :else (keyword key)))

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn roster-json-string-to-Roster [json-string]
  (json/read-str json-string
                 :value-fn my-value-reader
                 :key-fn my-key-reader))
(s/fdef roster-json-string-to-Roster
        :args (s/cat :json-string string?)
        :ret :unq/roster)

(ostest/instrument)
