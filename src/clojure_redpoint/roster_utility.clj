(ns clojure-redpoint.roster-utility
  (:require [clojure-csv.core :as csv]
            [clojure.string :as cs]))

(defn make-roster-list [roster-string]
  (let [de-spaced (cs/replace roster-string #", " ",")]
    (csv/parse-csv de-spaced)))
