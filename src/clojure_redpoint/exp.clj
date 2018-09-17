(ns clojure-redpoint.exp
  (:require [clojure.string :as str]))

(def roster-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")

(defn de-space [rs] (str/replace rs #", " ","))

(def lines (str/split-lines (de-space roster-string)))

(get lines 0)

(def info (get lines 0))

(def info-bad "The Beatles,")

(str/split info #",")

(str/split info-bad #",")

(def info-bad2 ",2014")

(str/split info-bad2 #",")

(def info-bad3 "The Beatles,2096")

(def info-bad4 "The Beatles,1896")

(def info-bad5 "The Beatles,Pony")

(str/split "" #",")

(str/split " " #",")

(def freqs (frequencies roster-string))

(count (filter #(= % \newline) roster-string))

(str/blank? roster-string)

(= 2 (count (str/split info #",")))

(get info 1)

(def year (read-string (get (str/split info #",") 1)))

(#(<= 1956 % 2056) year)

(<= 5 (count (filter #(= % \newline) roster-string)))

(defn string-valid?
  "Return true if string is not nil, empty or only spaces"
  [string]
  (not (str/blank? string)))

(defn roster-string-valid?
  "A valid string of >= 4 newlines?"
  [roster-string]
  (and (string-valid? roster-string)
       (<= 4 (count (filter #(= % \newline) roster-string)))))

(defn de-space
  "Remove the spaces between CSVs"
  [roster-string]
  (str/replace roster-string #", " ","))

(defn lines
  "Split string into lines"
  [roster-strng]
  (str/split-lines (de-space roster-string)))

(defn make-info-string
  "Return a string of first line if valid string parameter"
  [roster-string]
  (if (string-valid? roster-string)
    (->
      roster-string
      de-space
      lines
      (get 0))
    nil))

(def info (make-info-string roster-string))

(defn info-string-valid?
  "Return true if info-string not blank, name not blank and 1956 <= year <= 2056"
  [info-string]
  (and (string-valid? info-string)
       (->
         info-string
         (str/split #",")
         (count)
         (= 2))
       (->
         info-string
         (str/split #",")
         (get 0)
         (string-valid?))
       (->
         info-string
         (str/split #",")
         (get 1)
         (#(re-seq #"^[0-9]*$" %))
         (nil?)
         (not))
       (->
         info-string
         (str/split #",")
         (get 1)
         (Integer/parseInt)
         (#(<= 1956 % 2056)))))

(-> (/ 144 12) (/,,, 2 3) str keyword (list,,, :33) (count) (= 2))
