(ns clojure-redpoint.roster-utility
  (:require [clojure-csv.core :as csv]
            [clojure.string :as cs]))

(defn make-roster-list
  "Returns a lazy roster-list"
  [roster-string]
  (let [de-spaced (cs/replace roster-string #", " ",")]
    (csv/parse-csv de-spaced)))

(defn make-roster-info [roster-list]
  (first roster-list))

(defn make-players-list [roster-list]
  (rest roster-list))
