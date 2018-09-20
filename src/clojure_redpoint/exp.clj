(ns clojure-redpoint.exp
  (:require [clojure.string :as str]))

(def roster-string-ok "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(def roster-string-bad-length "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\n")
(def roster-string-bad-info1 "The Beatles\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info2 ",2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info3 "The Beatles,2096\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info4 "The Beatles, 1896\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info5 "The Beatles, 2014P\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info6 "\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")

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
    (str/trim-newline)))

(defn roster-string-valid?
  "A not-blank string of <= 4 newlines?"
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
  (and
    (not (nil? info-string))
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
          (#(re-seq #"^[0-9]*$" %))
          (nil?)
          (not))
        (->
          info-line
          (get 1)
          (Integer/parseInt)
          (#(<= 1956 % 2056)))))))

(defn master-roster-string-check?
  "Checks for valid rs then is"
  [roster-string]
  (and
    (roster-string-valid? roster-string)
    (info-string-valid? (make-info-string roster-string))))

(master-roster-string-check? roster-string-ok)
(master-roster-string-check? roster-string-bad-length)
(master-roster-string-check? roster-string-bad-info1)
(master-roster-string-check? roster-string-bad-info2)
(master-roster-string-check? roster-string-bad-info3)
(master-roster-string-check? roster-string-bad-info4)
(master-roster-string-check? roster-string-bad-info5)
(master-roster-string-check? roster-string-bad-info6)
