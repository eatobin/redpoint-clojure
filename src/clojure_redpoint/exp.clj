(ns clojure-redpoint.exp
  (:require [clojure.string :as str]))

(def roster-string-ok "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")

(def roster-string-bad-length "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc")

(def roster-string-bad-info1 "The Beatles\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")

(def roster-string-bad-info2 ",2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")

(def roster-string-bad-info3 "The Beatles,2096\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")

(def roster-string-bad-info4 "The Beatles, 1896\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")

(def roster-string-bad-info5 "The Beatles, Pony\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")

;(defn de-space [rs] (str/replace rs #", " ","))
;
;(def lines (str/split-lines (de-space roster-string)))
;
;(get lines 0)
;
;(def info (get lines 0))
;
;(def info-bad "The Beatles,")
;
;(str/split info #",")
;
;(str/split info-bad #",")
;
;(def info-bad2 ",2014")
;
;(str/split info-bad2 #",")
;
;(def info-bad3 "The Beatles,2096")
;
;(def info-bad4 "The Beatles,1896")
;
;(def info-bad5 "The Beatles,Pony")
;
;(str/split "" #",")
;
;(str/split " " #",")
;
;(def freqs (frequencies roster-string))
;
;(count (filter #(= % \newline) roster-string))
;
;(str/blank? roster-string)
;
;(= 2 (count (str/split info #",")))
;
;(get info 1)
;
;(def year (read-string (get (str/split info #",") 1)))
;
;(#(<= 1956 % 2056) year)
;
;(<= 5 (count (filter #(= % \newline) roster-string)))

(defn string-valid?
  "Return true if string is not nil, empty or only spaces"
  [string]
  (not (str/blank? string)))

(defn roster-string-valid?
  "A valid string of <= 4 newlines?"
  [roster-string]
  (and (string-valid? roster-string)
       (<= 4 (count (filter #(= % \newline) roster-string)))))

(defn de-space
  "Remove the spaces between CSVs"
  [roster-string]
  (str/replace roster-string #", " ","))

(defn lines
  "Split string into lines"
  [roster-string]
  (str/split-lines (de-space roster-string)))

(defn make-info-string
  "Return a string of first line if valid string parameter"
  [roster-string]
  (if (roster-string-valid? roster-string)
    (->
      roster-string
      de-space
      lines
      (get 0))
    nil))

;(def info (make-info-string roster-string))

(defn info-string-valid?
  "Return true if info-string not blank, name not blank and 1956 <= year <= 2056"
  [info-string]
  (and (string-valid? info-string)
       (let [info-line (->
                         info-string
                         (str/split #","))]
         (and
           (->
             info-line
             (count)
             (= 2))
           (->
             info-line
             (get 0)
             (string-valid?))
           (->
             info-line
             (get 1)
             (#(re-seq #"^[0-9]{4}$" %))
             (nil?)
             (not))
           (->
             info-line
             (get 1)
             (Integer/parseInt)
             (#(<= 1956 % 2056)))))))

(-> (/ 144 12) (/,,, 2 3) str keyword (list,,, :33) (count) (= 2))
