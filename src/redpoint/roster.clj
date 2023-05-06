(ns redpoint.roster
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]
            [redpoint.domain]))

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

(defn roster-json-string-to-Roster
  [[error-string json-string]]
  (if (nil? error-string)
    (try
      [nil (json/read-str json-string
                          :value-fn my-value-reader
                          :key-fn my-key-reader)]
      (catch Exception e
        [(str (.getMessage e)) nil]))
    [error-string nil]))
(s/fdef roster-json-string-to-Roster
        :args (s/cat :input (s/or :success-in (s/tuple nil? string?)
                                  :failure-in (s/tuple string? nil?)))
        :ret (s/or :success-out (s/tuple nil? :unq/roster)
                   :failure-out (s/tuple string? nil?)))

(ostest/instrument)
