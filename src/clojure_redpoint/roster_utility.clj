(ns clojure-redpoint.roster-utility
  (:require [clojure-redpoint.domain :as dom]
            [clojure-csv.core :as csv]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [clojure.repl :refer :all]
            [clojure.string :as str]))

(def roster-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(def roster-string-bad-length "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\n")
(def roster-string-bad-info1 "The Beatles\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info2 ",2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info3 "The Beatles,2096\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info4 "The Beatles, 1896\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info5 "The Beatles, 2014P\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info6 "\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen")
(def roster-string-bad-info7 "The Beatles, 2014\nRinStaX, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(def roster-string-bad-info8 "The Beatles, 2014\nRinSta, Ringo Starr, JohLen\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")

(defn clean-string
  "Ensure string is not nil, empty or only spaces"
  [raw-string]
  (if (str/blank? raw-string)
    [nil "The roster string was nil, empty or only spaces"]
    [raw-string nil]))

(defn scrub
  "Remove the spaces between CSVs and any final \n"
  [raw-string]
  (->
   raw-string
   (str/replace #", " ",")
   (str/trim-newline)))

(defn valid-length-raw-string
  "A string of <= 4 newlines?"
  [raw-string]
  (if (<= 4 (count (filter #(= % \newline) (scrub raw-string))))
    [raw-string nil]
    [nil "Roster string is not long enough"]))

;; (defn not-blank-string?
;;   "Return true if string is not nil, empty or only spaces"
;;   [raw-string]
;;   (not (str/blank? raw-string)))

;; (defn non-blank-raw-string
;;   "Return the string if string is not nil, empty or only spaces"
;;   [raw-string]
;;   (if (not-blank-string? raw-string)
;;     [raw-string nil]
;;     [nil "The roster string was nil, empty or only spaces"]))


;(defn valid-roster-string?
;  "A not-blank string of <= 4 newlines?"
;  [raw-string]
;  (and (not-blank-string? raw-string)
;       (<= 4 (count (filter #(= % \newline) (scrub raw-string))))))

(defn apply-or-error [f [val err]]
  (if (nil? err)
    (f val)
    [nil err]))

(defn valid-roster-string
  [raw-string]
  (let [result (clean-string raw-string)
        result (apply-or-error valid-length-raw-string result)]
    result))

;; (defn valid-roster-string
;;   "A not-blank string of <= 4 newlines?"
;;   [raw-string]
;;   (let [[raw-string err1] (non-blank-raw-string raw-string)
;;         [raw-string err2] (if (nil? err1) (valid-length-raw-string raw-string) [nil err1])]
;;     [raw-string err2]))

(defn lines
  "Split string into lines"
  [raw-string]
  (str/split-lines (scrub raw-string)))

(defn make-info-string
  "Return a string of first line if valid string parameter"
  [raw-string]
  (let [[raw-string2 err] (valid-roster-string raw-string)]
    (if (nil? err)
      (->
       raw-string2
       scrub
       lines
       (get 0))
      [nil "Received an invalid roster-string"])))

(defn year-present
  "Return the raw-info-string if a year value is present"
  [raw-info-string]
  (let [info-line (->
                   raw-info-string
                   (str/split #","))]
    (if (->
         info-line
         (count)
         (= 2))
      [raw-info-string nil]
      [nil "The year value is missing"])))

(defn name-present
  "Return the raw-info-string if a name value is present"
  [raw-info-string]
  (let [info-line (->
                   raw-info-string
                   (str/split #","))]
    (if (->
         info-line
         (get 0)
         (not-blank-string?))
      [raw-info-string nil]
      [nil "The name value is missing"])))

(defn year-text-all-digits
  "Return the raw-info-string if the year text all digits"
  [raw-info-string]
  (let [info-line (->
                   raw-info-string
                   (str/split #","))]
    (if (->
         info-line
         (get 1)
         (#(re-seq #"^[0-9]*$" %))
         (nil?)
         (not))
      [raw-info-string nil]
      [nil "The year value is not all digits"])))

(defn valid-info-string?
  "Return true if info-string not blank, name not blank and 1956 <= year <= 2056"
  [raw-string]
  (let [raw-info-string (make-info-string raw-string)]
    (and
     (not (nil? raw-info-string))
     (let [info-line (->
                      raw-info-string
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
         (#(<= 1956 % 2056))))))))

(defn vec-remove
  "Remove elem in coll"
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn pure-player-symbols
  "Given a valid roster-string, return a vector of player vectors"
  [valid-roster-string]
  (vec-remove (->>
               (str/split-lines (scrub valid-roster-string))
               (map #(str/split % #","))
               (into []))
              0))

(defn all-six-chars?
  "All strings in the vector are 6 chars long"
  [coll]
  (and (= 3 (count coll))
       (= 3 (count (filter #(= (count %) 6) coll)))))

(defn remove-name
  "Given a player vector, return the vector without the player name"
  [player-vector]
  (vec-remove player-vector 1))

(defn player-test?
  "Checks the validity of the player sub-string given a roster string"
  [valid-roster-string]
  (->>
   valid-roster-string
   (pure-player-symbols)
   (map remove-name)
   (map all-six-chars?)
   (every? true?)))

(defn master-string-check?
  "Given a string,
  checks for valid roster-string
  then info-string sub-string and
  player-string sub-string"
  [string]
  (and
   (valid-roster-string string)
   (valid-info-string? string)
   (player-test? string)))

(defn make-roster-seq
  "Returns a lazy roster-seq"
  [string]
  (if (master-string-check? string)
    (csv/parse-csv (scrub string))
    nil))
(s/fdef make-roster-seq
        :args (s/or :not-nil (s/cat :roster-string ::dom/roster-string)
                    :nil (s/cat :roster-string nil?))
        :ret (s/or :seq ::dom/roster-seq
                   :nil nil?))

(defn extract-roster-info-vector
  "Given a roster-sequence, returns a vector containing the roster name and year"
  [roster-sequence]
  (let [res (first roster-sequence)]
    (if (nil? res)
      nil
      res)))
(s/fdef extract-roster-info-vector
        :args (s/or :not-nil (s/cat :roster-sequence ::dom/roster-seq)
                    :nil (s/cat :roster-sequence nil?))
        :ret (s/or :line ::dom/roster-line
                   :nil nil?))

(defn extract-players-list
  "Returns a list of vectors - each vector a player symbol, player name, first
  givee and first giver"
  [roster-sequence]
  (let [res (rest roster-sequence)]
    (if (= res '())
      '()
      (into () (rest roster-sequence)))))
(s/fdef extract-players-list
        :args (s/or :not-nil (s/cat :roster-sequence ::dom/roster-seq)
                    :nil (s/cat :roster-seq nil?))
        :ret ::dom/plrs-list)

(defn make-gift-pair
  "Returns a gift pair hash map given givee and giver as strings"
  [givee giver]
  (hash-map
   :givee (keyword givee)
   :giver (keyword giver)))
(s/fdef make-gift-pair
        :args (s/cat :givee string? :giver string?)
        :ret :unq/gift-pair)

(defn make-player
  "Returns a player hash map given a player name (string) and a gift history
  (vector of gift pairs)"
  [p-name g-hist]
  (hash-map
   :name p-name
   :gift-history g-hist))
(s/fdef make-player
        :args (s/cat :p-name ::dom/name :g-hist :unq/gift-history)
        :ret :unq/player)

(defn make-player-map
  "Returns a hash map linking a player symbol (key) to a player hash map (value)
  given a player symbol, name, initial givee and initial giver - all strings"
  [[s n ge gr]]
  (let [gp (make-gift-pair ge gr)
        plr (make-player n (vector gp))]
    (hash-map
     (keyword s) plr)))
(s/fdef make-player-map
        :args (s/cat :arg1 ::dom/plr-map-vec)
        :ret ::dom/plr-map)

(defn make-players-map
  "Returns a hash map of multiple players given a roster string"
  [players-list]
  (into {} (map make-player-map players-list)))
(s/fdef make-players-map
        :args (s/cat :players-list ::dom/plrs-list)
        :ret ::dom/plr-map)

(defn get-player-in-roster
  "Returns a player given a players map and a player symbol"
  [plrs-map plr-sym]
  (get plrs-map plr-sym))
(s/fdef get-player-in-roster
        :args (s/cat :plrs-map ::dom/plr-map :plr-sym keyword?)
        :ret :unq/player)

(defn get-gift-history-in-player
  "Returns a gift history given a player"
  [plr]
  (get plr :gift-history))
(s/fdef get-gift-history-in-player
        :args (s/cat :plr :unq/player)
        :ret :unq/gift-history)

(defn get-gift-pair-in-gift-history
  "Returns a gift pair given a gift history and a gift year"
  [g-hist g-year]
  (get g-hist g-year))
(s/fdef get-gift-pair-in-gift-history
        :args (s/and
               (s/cat :g-hist :unq/gift-history
                      :g-yearX (s/and int? #(> % -1)))
               #(<= (:g-yearX %) (count (:g-hist %))))
        :ret :unq/gift-pair)

(defn get-gift-pair-in-roster
  "Returns a gift pair given a player map, a player symbol and a gift year"
  [plrs-map plr-sym g-year]
  (let [plr (get-player-in-roster plrs-map plr-sym)
        gh (get-gift-history-in-player plr)]
    (get-gift-pair-in-gift-history gh g-year)))
(s/fdef get-gift-pair-in-roster
        :args (s/cat :plrs-map ::dom/plr-map
                     :plr-sym keyword?
                     :g-year (s/and int? #(> % -1)))
        :ret :unq/gift-pair)

(defn get-givee-in-gift-pair
  "Returns a givee given a gift pair"
  [g-pair]
  (get g-pair :givee))
(s/fdef get-givee-in-gift-pair
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::dom/givee)

(defn get-giver-in-gift-pair
  "Returns a giver given a gift pair"
  [g-pair]
  (get g-pair :giver))
(s/fdef get-giver-in-gift-pair
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::dom/giver)

(defn set-gift-history-in-player
  "Sets a gift history into the provided player"
  [g-hist plr]
  (assoc plr :gift-history g-hist))
(s/fdef set-gift-history-in-player
        :args (s/cat :g-hist :unq/gift-history
                     :plr :unq/player)
        :ret :unq/player)

(defn set-gift-pair-in-gift-history
  "Returns a gift history with the provided gift pair at the supplied year"
  [g-hist g-year g-pair]
  (assoc g-hist g-year g-pair))
(s/fdef set-gift-pair-in-gift-history
        :args (s/and
               (s/cat :g-hist :unq/gift-history
                      :g-year (s/and int? #(> % -1))
                      :g-pair :unq/gift-pair)
               #(<= (:g-year %) (count (:g-hist %))))
        :ret :unq/gift-history)

(defn set-gift-pair-in-roster
  "Returns a player roster with a gift pair set for the player and year"
  [plrs-map plr-sym g-year g-pair]
  (let [plr (get-player-in-roster plrs-map plr-sym)
        gh (get-gift-history-in-player plr)
        ngh (set-gift-pair-in-gift-history gh g-year g-pair)
        nplr (set-gift-history-in-player ngh plr)]
    (assoc plrs-map plr-sym nplr)))
(s/fdef set-gift-pair-in-roster
        :args (s/cat :plrs-map ::dom/plr-map
                     :plr-sym keyword?
                     :g-year (s/and int? #(> % -1))
                     :g-pair :unq/gift-pair)
        :ret ::dom/plr-map)

(defn check-give
  "Returns true if a players map contains a valid giver,
  givee and year"
  [plrs-map giver g-year givee]
  (let [plr (get-player-in-roster plrs-map giver)
        gh (get-gift-history-in-player plr)
        h-len (count gh)]
    (and (contains? plrs-map giver)
         (contains? plrs-map givee)
         (<= (+ g-year 1) h-len))))
(s/fdef check-give
        :args (s/cat :plrs-map ::dom/plr-map
                     :giver keyword?
                     :g-year (s/and int? #(> % -1))
                     :givee keyword?)
        :ret boolean?)

(defn add-year-in-player
  "Adds a new placeholder year to the end of a player's gift history"
  [plr]
  (let [gh (get-gift-history-in-player plr)
        ngh (conj gh {:giver :none, :givee :none})]
    (set-gift-history-in-player ngh plr)))
(s/fdef add-year-in-player
        :args (s/cat :plr :unq/player)
        :ret :unq/player)

(defn add-year-in-roster
  "Add a year for each player in roster"
  [plrs-map]
  (into {}
        (for [[k v] plrs-map]
          [k (add-year-in-player v)])))
(s/fdef add-year-in-roster
        :args (s/cat :plrs-map ::dom/plr-map)
        :ret ::dom/plr-map)

(st/instrument)

(def params {:address "address" :email "email@email.com" :phone "(555) 555-5555" :state "WA"})

(defn clean-address [params]
  (if (empty? (params :address))
    [nil "Please enter your address"]
    [params nil]))

(defn clean-email [params]
  (if (re-find #".+\@.+\..+" (params :email))
    [params nil]
    [nil "Please enter an email address"]))

(defn clean-phone [params]
  (if (re-find #"\([0-9]{3}\) [0-9]{3}-[0-9]{4}" (params :phone))
    [params nil]
    [nil "Please enter your phone number in (555) 555-5555 format."]))

(defn clean-state [params]
  (case (params :state)
    "WA" [params nil]
    "OR" [params nil]
    [nil "We only want people from Oregon or Washington, for some reason."]))

(defn clean-contact [params]
  (let [[params err1] (clean-email params)
        [params err2] (if (nil? err1) (clean-address params) [nil err1])
        [params err3] (if (nil? err2) (clean-phone params) [nil err2])
        [params err4] (if (nil? err3) (clean-state params) [nil err3])]
    [params err4]))

(defn clean-contact-f [params]
  (let [result1 (clean-email params)
        result2 (apply-or-error clean-address result1)
        result3 (apply-or-error clean-phone result2)
        result4 (apply-or-error clean-state result3)]
    result4))

(clean-contact params)
(clean-contact-f params)
