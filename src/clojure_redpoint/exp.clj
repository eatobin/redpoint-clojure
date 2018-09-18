(ns clojure-redpoint.exp
  (:require [clojure.string :as str]))

(def roster-string-ok "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(def roster-string-bad-length "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\n")
(def roster-string-bad-info1 "The Beatles\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info2 ",2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info3 "The Beatles,2096\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info4 "The Beatles, 1896\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info5 "The Beatles, Pony\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")

;(-> (/ 144 12) (/,,, 2 3) str keyword (list,,, :33) (count) (= 2))

(defn not-blank-string?
  "Return true if string is not nil, empty or only spaces"
  [string]
  (not (str/blank? string)))

(defn scrub
  "Remove the spaces between CSVs and any final \n"
  [roster-string]
  (->
    roster-string
    (str/replace #", " ",")
    (str/trim)))

(defn roster-string-valid?
  "A valid string of <= 4 newlines?"
  [roster-string]
  (and (not-blank-string? roster-string)
       (<= 4 (count (filter #(= % \newline) (scrub roster-string))))))

(defn lines
  "Split string into lines"
  [roster-string]
  (str/split-lines (scrub roster-string)))

(defn make-info-string
  "Return a string of first line if valid string parameter"
  [roster-string]
  (if (roster-string-valid? roster-string)
    (->
      roster-string
      scrub
      lines
      (get 0))
    nil))

(defn info-string-valid?
  "Return true if info-string not blank, name not blank and 1956 <= year <= 2056"
  [info-string]
  (and (not-blank-string? info-string)
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
             (not-blank-string?))
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

(info-string-valid? (make-info-string roster-string-ok))
(info-string-valid? (make-info-string roster-string-bad-length))
(info-string-valid? (make-info-string roster-string-bad-info1))
(info-string-valid? (make-info-string roster-string-bad-info2))
(info-string-valid? (make-info-string roster-string-bad-info3))
(info-string-valid? (make-info-string roster-string-bad-info4))
(info-string-valid? (make-info-string roster-string-bad-info5))
