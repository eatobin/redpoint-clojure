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

(defn apply-or-error [f [val err]]
  (if (nil? err)
    (f val)
    [nil err]))

(defn scrub
  "Remove the spaces between CSVs and any final \n"
  [raw-string]
  (->
    raw-string
    (str/replace #", " ",")
    (str/trim-newline)))

(defn lines
  "Split string into lines"
  [scrubbed]
  (str/split-lines scrubbed))

(defn non-blank-string
  "Ensure string is not nil, empty or only spaces. Returns a scrubbed string"
  [raw-string]
  (if (str/blank? raw-string)
    [nil "The roster string was nil, empty or only spaces"]
    [(scrub raw-string) nil]))

(defn valid-length-string
  "A string of newlines >= 4?"
  [scrubbed]
  (if (<= 4 (count (filter #(= % \newline) scrubbed)))
    [scrubbed nil]
    [nil "Roster string is not long enough"]))

;(defn valid-roster-string
;  "Ensure that raw-string is not blank and long enough"
;  [raw-string]
;  (let [result (non-blank-string raw-string)
;        result (apply-or-error valid-length-string result)]
;    result))



(defn name-present
  "Return the raw-string if a name value is present"
  [raw-string]
  (let [scrubbed (scrub raw-string)]
    (let [info-vector (->
                        scrubbed
                        lines
                        (get 0)
                        (str/split #","))]
      (if (->
            info-vector
            (get 0)
            (non-blank-string)
            (get 1)
            (nil?))
        [raw-string nil]
        [nil "The name value is missing"]))))

(defn year-present
  "Return the info-string if a year value is present"
  [info-string]
  (let [info-vector (str/split (scrub info-string) #",")]
    (if (= 2 (count info-vector))
      [info-string nil]
      [nil "The year value is missing"])))

(defn year-text-all-digits
  "Return the raw-info-string if the year text all digits"
  [info-string]
  (let [info-vector (str/split (scrub info-string) #",")]
    (if (->
          info-vector
          (get 1)
          (#(re-seq #"^[0-9]*$" %))
          (nil?)
          (not))
      [info-string nil]
      [nil "The year value is not all digits"])))

(defn year-in-range
  "Return the info-string if 1956 <= year <= 2056"
  [info-string]
  (let [info-vector (str/split (scrub info-string) #",")]
    (if (->
          info-vector
          (get 1)
          (Integer/parseInt)
          (#(<= 1956 % 2056)))
      [info-string nil]
      [nil "Not 1956 <= year <= 2056"])))

;(defn make-info-string
;  "Return a string of first line if valid string parameter"
;  [raw-string]
;  (let [[raw-string err] (valid-roster-string raw-string)]
;    (if (nil? err)
;      (conj [] (->
;                 raw-string
;                 scrub
;                 lines
;                 (get 0)) nil)
;      [nil "Received an invalid roster-string"])))
;
;(defn make-valid-info-string
;  "Return a valid info-string or error from a raw-string"
;  [raw-string]
;  (let [result (make-info-string raw-string)
;        result (apply-or-error name-present result)
;        result (apply-or-error year-present result)
;        result (apply-or-error year-text-all-digits result)
;        result (apply-or-error year-in-range result)]
;    result))

(defn vec-remove
  "Remove elem in coll"
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

;(defn make-player-vectors
;  "Given a valid raw-string, return a vector of player vectors"
;  [raw-string]
;  (let [[raw-string err] (valid-roster-string raw-string)]
;    (if (nil? err)
;      (conj []
;            (vec-remove (->>
;                          (str/split-lines (scrub raw-string))
;                          (map #(str/split % #","))
;                          (into []))
;                        0)
;            nil)
;      [nil "Received an invalid roster-string"])))

(defn remove-name
  "Given a player vector, return the vector without the player name"
  [player-vector]
  (vec-remove player-vector 1))

(defn only-symbols
  "Returns player vectors void of names - symbols only"
  [[player-vectors err]]
  (if (nil? err)
    (conj []
          (map remove-name player-vectors)
          nil)
    [nil "Could not remove a name"]))

(defn all-six-chars?
  "All strings in the vector are 6 chars long"
  [player-symbols]
  (and (= 3 (count player-symbols))
       (= 3 (count (filter #(= (count %) 6) player-symbols)))))

(defn all-vectors-all-six?
  "All of the vectors only symbols"
  [[player-vectors err]]
  (if (nil? err)
    (map all-six-chars? player-vectors)
    false))



;(defn player-test
;  "Checks the validity of the player sub-string given a roster string"
;  [raw-string]
;  (let [[vectors err] (make-player-vectors raw-string)]
;    (if (nil? err)
;      (if (->>
;            vectors
;            (only-symbols)
;            (all-vectors-all-six?)
;            (every? true?))
;        [(scrub raw-string) nil]
;        [nil "Player substring is incorrect"])
;      [nil "Received a bad roster string"])))
;
;
;(defn make-valid-player-vectors
;  "Return a valid player vector or error from a raw-string"
;  [raw-string]
;  (let [result (make-player-vectors raw-string)
;        result (apply-or-error player-test result)]
;    result))

;(defn master-string-check?
;  "Given a string,
;  checks for valid roster-string
;  then info-string sub-string and
;  player-string sub-string"
;  [string]
;  (and
;    (valid-roster-string string)
;    (valid-info-string? string)
;    (player-test? string)))

;(defn make-roster-seq
;  "Returns a lazy roster-seq"
;  [string]
;  (if (master-string-check? string)
;    (csv/parse-csv (scrub string))
;    nil))
;(s/fdef make-roster-seq
;        :args (s/or :not-nil (s/cat :roster-string ::dom/roster-string)
;                    :nil (s/cat :roster-string nil?))
;        :ret (s/or :seq ::dom/roster-seq
;                   :nil nil?))

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
