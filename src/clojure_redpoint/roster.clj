(ns clojure-redpoint.roster
  (:require [clojure-redpoint.domain :as dom]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [clojure.repl :refer :all]
            [clojure.string :as str]
            [clojure-csv.core :as csv]
            [clojure-redpoint.roster-string-check :refer [vec-remove]]))

;(def scrubbed "The Beatles,2014\nRinSta,Ringo Starr,JohLen,GeoHar\nJohLen,John Lennon,PauMcc,RinSta\nGeoHar,George Harrison,RinSta,PauMcc\nPauMcc,Paul McCartney,GeoHar,JohLen")
;
;(def players-map {:PauMcc {:name         "Paul McCartney",
;                           :gift-history [{:giver :JohLen, :givee :GeoHar}]},
;                  :GeoHar {:name         "George Harrison",
;                           :gift-history [{:giver :PauMcc, :givee :RinSta}]},
;                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                  :RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})
;
;(def players-vector [["RinSta" "Ringo Starr" "JohLen" "GeoHar"]
;                     ["JohLen" "John Lennon" "PauMcc" "RinSta"]
;                     ["GeoHar" "George Harrison" "RinSta" "PauMcc"]
;                     ["PauMcc" "Paul McCartney" "GeoHar" "JohLen"]])
;
;(def players-map-ge {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                     :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                     :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :GeoHar}]},
;                     :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})
;
;(def players-map-gr {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
;                     :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
;                     :GeoHar {:name "George Harrison", :gift-history [{:giver :GeoHar, :givee :RinSta}]},
;                     :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})
;
;(def players-map-add {:RinSta {:name         "Ringo Starr",
;                               :gift-history [{:giver :GeoHar, :givee :JohLen}
;                                              {:giver :none, :givee :none}]},
;                      :JohLen {:name         "John Lennon",
;                               :gift-history [{:giver :RinSta, :givee :PauMcc}
;                                              {:giver :none, :givee :none}]},
;                      :GeoHar {:name         "George Harrison",
;                               :gift-history [{:giver :PauMcc, :givee :RinSta}
;                                              {:giver :none, :givee :none}]},
;                      :PauMcc {:name         "Paul McCartney",
;                               :gift-history [{:giver :JohLen, :givee :GeoHar}
;                                              {:giver :none, :givee :none}]}})
;
;(def player {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]})

(defn get-roster-name
  "Given a scrubbed return the roster name"
  [scrubbed]
  (->
    scrubbed
    (str/split-lines)
    (get 0)
    (str/split #",")
    (first)))
(s/fdef get-roster-name
        :args (s/cat :scrubbed ::dom/scrubbed)
        :ret string?)

(defn get-roster-year
  "Given a scrubbed return the roster year"
  [scrubbed]
  (->
    scrubbed
    (str/split-lines)
    (get 0)
    (str/split #",")
    (last)))
(s/fdef get-roster-year
        :args (s/cat :scrubbed ::dom/scrubbed)
        :ret string?)

(defn make-players-vector
  "Given a scrubbed return the player-vector"
  [scrubbed]
  (->
    scrubbed
    (str/split-lines)
    (vec-remove 0)
    (->>
      (map #(csv/parse-csv %))
      (map first)
      (into []))))
(s/fdef make-players-vector
        :args (s/cat :scrubbed ::dom/scrubbed)
        :ret ::dom/plrs-vector)

(defn make-gift-pair
  "Returns a gift pair hash map given givee and giver as strings or keywords"
  [givee giver]
  (hash-map
    :givee (keyword givee)
    :giver (keyword giver)))
(s/fdef make-gift-pair
        :args (s/or :strings (s/cat :givee string? :giver string?)
                    :keywords (s/cat :givee keyword? :giver keyword?))
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
  "Returns a hash map of multiple players given a players-vector"
  [players-vector]
  (into {} (map make-player-map players-vector)))
(s/fdef make-players-map
        :args (s/cat :players-vector ::dom/plrs-vector)
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

;(defn check-give
;  "Returns true if a players map contains a valid giver,
;  givee and year"
;  [plrs-map giver g-year givee]
;  (let [plr (get-player-in-roster plrs-map giver)
;        gh (get-gift-history-in-player plr)
;        h-len (count gh)]
;    (and (contains? plrs-map giver)
;         (contains? plrs-map givee)
;         (<= (+ g-year 1) h-len))))
;(s/fdef check-give
;        :args (s/cat :plrs-map ::dom/plr-map
;                     :giver keyword?
;                     :g-year (s/and int? #(> % -1))
;                     :givee keyword?)
;        :ret boolean?)

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

(defn get-player-name-in-roster [plrs-map plr-sym]
  (let [plr (get-player-in-roster plrs-map plr-sym)]
    (get plr :name)))





(defn get-givee-in-roster [plrs-map plr-sym g-year]
  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
    (get-givee-in-gift-pair gp)))

(defn get-giver-in-roster [plrs-map plr-sym g-year]
  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
    (get-giver-in-gift-pair gp)))

;(defn set-givee-in-roster [plrs-map plr-sym g-year ge]
;  (if (check-give plrs-map plr-sym g-year ge)
;    (let [gr (get-giver-in-roster plrs-map plr-sym g-year)
;          gp (make-gift-pair ge gr)]
;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;    plrs-map))

(defn set-givee-in-roster [plrs-map plr-sym g-year ge]
  (let [gr (get-giver-in-roster plrs-map plr-sym g-year)
        gp (make-gift-pair ge gr)]
    (set-gift-pair-in-roster plrs-map plr-sym g-year gp)))

;;(defn set-giver-in-roster [plrs-map plr-sym g-year gr]
;;  (if (check-give plrs-map plr-sym g-year gr)
;;    (let [ge (get-givee-in-roster plrs-map plr-sym g-year)
;;          gp (make-gift-pair ge gr)]
;;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;;    plrs-map))
;
(st/instrument)
;
;
;;(s/conform vector?
;;           (extract-roster-info-vector rs))
;;(s/conform nil?
;;           (extract-roster-info-vector ""))
;;(s/conform :unq/gift-pair
;;           (make-gift-pair "one" "two"))
;;(s/conform ::plrs-list
;;           (extract-players-list rs))
;;(s/conform ::plrs-list
;;           (extract-players-list ""))
;;(def x (make-gift-pair "joe" "bob"))
;;(def y (make-gift-pair "joey" "bobby"))
;;(def h [x y])
;;(s/conform :unq/gift-history h)
;;(s/conform :unq/player
;;           (make-player "eric" h))
;;(s/conform ::plr-map (make-player-map ["s" "n" "ge" "gr"]))
;;(def pm (make-players-map rs))
;;(s/conform (s/or :found :unq/player
;;                 :not-found nil?)
;;           (get-player-in-roster pm :GeoHar))
;;; =>
;;; [:found
;;;  {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}]
;;(s/conform (s/or :found :unq/player
;;                 :not-found nil?)
;;           (get-player-in-roster pm :GeoHarX))
;;; => [:not-found nil]
;;(s/conform ::givee
;;           (get-givee-in-gift-pair {:giver :PauMcc, :givee :RinSta}))
;;(s/conform :unq/player
;;           (set-gift-history-in-player [{:giver :RinSta, :givee :PauMcc}] {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}))
;;(stest/check `set-gift-pair-in-gift-history)
