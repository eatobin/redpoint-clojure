(ns clojure-redpoint.roster
  (:require [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::roster-name string?)
(s/def ::roster-year int?)
(s/def ::player-key symbol?)
(s/def :unq/players (s/map-of ::player-key :unq/player))
(s/def :unq/roster (s/keys :req-un [::roster-name ::roster-year :unq/players]))


(defn get-roster-name
  "Given a roster return the roster name"
  [roster]
  roster :roster-name)
(s/fdef get-roster-name
        :args (s/cat :roster :unq/roster)
        :ret ::roster-name)

(defn get-roster-year
  "Given a roster return the roster year"
  [roster]
  roster ::roster-year)
(s/fdef get-roster-year
        :args (s/cat :roster :unq/roster)
        :ret ::roster-year)

;(defn make-players-vector
;  "Given a scrubbed return the player-vector"
;  [scrubbed]
;  (->
;    scrubbed
;    (str/split-lines)
;    (rsc/vec-remove 0)
;    (->>
;      (map #(csv/parse-csv %))
;      (map first)
;      (into []))))
;(s/fdef make-players-vector
;        :args (s/cat :scrubbed ::rsc/scrubbed)
;        :ret ::plrs-vector)
;
;;(defn make-gift-pair
;;  "Returns a gift pair hash map given givee and giver as strings or keywords"
;;  [givee giver]
;;  (hash-map
;;    :givee (keyword givee)
;;    :giver (keyword giver)))
;;(s/fdef make-gift-pair
;;        :args (s/or :strings (s/cat :givee string? :giver string?)
;;                    :keywords (s/cat :givee keyword? :giver keyword?))
;;        :ret :unq/gift-pair)
;
;(defn make-gift-pair
;  "Returns a gift pair hash map given givee and giver as strings or keywords"
;  [givee giver]
;  (hash-map
;    :givee (keyword givee)
;    :giver (keyword giver)))
;(s/fdef make-gift-pair
;        :args (s/cat :givee string? :giver string?)
;        :ret :unq/gift-pair)
;
;(defn make-player
;  "Returns a player hash map given a player name (string) and a gift history
;  (vector of gift pairs)"
;  [p-name g-hist]
;  (hash-map
;    :name p-name
;    :gift-history g-hist))
;(s/fdef make-player
;        :args (s/cat :p-name ::name :g-hist :unq/gift-history)
;        :ret :unq/player)
;
;(defn make-player-map
;  "Returns a hash map linking a player symbol (key) to a player hash map (value)
;  given a player symbol, name, initial givee and initial giver - all strings"
;  [[s n ge gr]]
;  (let [gp (make-gift-pair ge gr)
;        plr (make-player n (vector gp))]
;    (hash-map
;      (keyword s) plr)))
;(s/fdef make-player-map
;        :args (s/cat :arg1 ::plr-map-vec)
;        :ret ::plr-map)
;
;(defn make-players-map
;  "Returns a hash map of multiple players given a players-vector"
;  [players-vector]
;  (into {} (map make-player-map players-vector)))
;(s/fdef make-players-map
;        :args (s/cat :players-vector ::plrs-vector)
;        :ret ::plr-map)
;
;(defn get-player-in-roster
;  "Returns a player given a players map and a player symbol"
;  [plrs-map plr-sym]
;  (get plrs-map plr-sym))
;(s/fdef get-player-in-roster
;        :args (s/cat :plrs-map ::plr-map :plr-sym keyword?)
;        :ret (s/or :found :unq/player
;                   :not-found nil?))
;
;(defn get-gift-history-in-player
;  "Returns a gift history given a player"
;  [plr]
;  (get plr :gift-history))
;(s/fdef get-gift-history-in-player
;        :args (s/cat :plr :unq/player)
;        :ret (s/or :found :unq/gift-history
;                   :not-found nil?))
;
;(defn get-gift-pair-in-gift-history
;  "Returns a gift pair given a gift history and a gift year"
;  [g-hist g-year]
;  (get g-hist g-year))
;(s/fdef get-gift-pair-in-gift-history
;        :args (s/and
;                (s/cat :g-hist :unq/gift-history
;                       :g-yearX (s/and int? #(> % -1)))
;                #(<= (:g-yearX %) (count (:g-hist %))))
;        :ret (s/or :found :unq/gift-pair
;                   :not-found nil?))
;
;(defn get-gift-pair-in-roster
;  "Returns a gift pair given a player map, a player symbol and a gift year"
;  [plrs-map plr-sym g-year]
;  (let [plr (get-player-in-roster plrs-map plr-sym)
;        gh (get-gift-history-in-player plr)]
;    (get-gift-pair-in-gift-history gh g-year)))
;(s/fdef get-gift-pair-in-roster
;        :args (s/cat :plrs-map ::plr-map
;                     :plr-sym keyword?
;                     :g-year ::g-year)
;        :ret (s/or :found :unq/gift-pair
;                   :not-found nil?))
;

;(defn set-gift-history-in-player
;  "Sets a gift history into the provided player"
;  [g-hist plr]
;  (assoc plr :gift-history g-hist))
;(s/fdef set-gift-history-in-player
;        :args (s/cat :g-hist :unq/gift-history
;                     :plr :unq/player)
;        :ret :unq/player)
;
;(defn set-gift-pair-in-gift-history
;  "Returns a gift history with the provided gift pair at the supplied year"
;  [g-hist g-year g-pair]
;  (assoc g-hist g-year g-pair))
;(s/fdef set-gift-pair-in-gift-history
;        :args (s/and
;                (s/cat :g-hist :unq/gift-history
;                       :g-year ::g-year
;                       :g-pair :unq/gift-pair)
;                #(<= (:g-year %) (count (:g-hist %))))
;        :ret :unq/gift-history)
;
;(defn set-gift-pair-in-roster
;  "Returns a player roster with a gift pair set for the player and year"
;  [plrs-map plr-sym g-year g-pair]
;  (let [plr (get-player-in-roster plrs-map plr-sym)
;        gh (get-gift-history-in-player plr)
;        ngh (set-gift-pair-in-gift-history gh g-year g-pair)
;        nplr (set-gift-history-in-player ngh plr)]
;    (assoc plrs-map plr-sym nplr)))
;(s/fdef set-gift-pair-in-roster
;        :args (s/cat :plrs-map ::plr-map
;                     :plr-sym keyword?
;                     :g-year ::g-year
;                     :g-pair :unq/gift-pair)
;        :ret ::plr-map)
;
;(defn add-year-in-player
;  "Adds a new placeholder year to the end of a player's gift history"
;  [plr plr-sym]
;  (let [gh (get-gift-history-in-player plr)
;        ngh (conj gh {:giver plr-sym, :givee plr-sym})]
;    (set-gift-history-in-player ngh plr)))
;(s/fdef add-year-in-player
;        :args (s/cat :plr :unq/player :plr-sym keyword?)
;        :ret :unq/player)
;
;(defn add-year-in-roster
;  "Add a year for each player in roster"
;  [plrs-map]
;  (into {}
;        (for [[k v] plrs-map]
;          [k (add-year-in-player v k)])))
;(s/fdef add-year-in-roster
;        :args (s/cat :plrs-map ::plr-map)
;        :ret ::plr-map)
;
;(defn get-player-name-in-roster [plrs-map plr-sym]
;  (let [plr (get-player-in-roster plrs-map plr-sym)]
;    (get plr :name)))
;(s/fdef get-player-name-in-roster
;        :args (s/cat :plrs-map ::plr-map :plr-sym keyword?)
;        :ret ::name)
;
;(defn get-givee-in-roster [plrs-map plr-sym g-year]
;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;    (get-givee-in-gift-pair gp)))
;(s/fdef get-givee-in-roster
;        :args (s/cat :plrs-map ::plr-map :plr-sym keyword? :g-year ::g-year)
;        :ret ::givee)
;
;(defn get-giver-in-roster [plrs-map plr-sym g-year]
;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;    (get-giver-in-gift-pair gp)))
;(s/fdef get-giver-in-roster
;        :args (s/cat :plrs-map ::plr-map :plr-sym keyword? :g-year ::g-year)
;        :ret ::giver)
;
;(defn set-givee-in-roster [plrs-map plr-sym g-year ge]
;  (let [gr (get-giver-in-roster plrs-map plr-sym g-year)
;        gp (make-gift-pair (name ge) (name gr))]
;    (set-gift-pair-in-roster plrs-map plr-sym g-year gp)))
;(s/fdef set-givee-in-roster
;        :args (s/cat :plrs-map ::plr-map :plr-sym keyword?
;                     :g-year ::g-year :ge ::givee)
;        :ret ::plr-map)
;
;(defn set-giver-in-roster [plrs-map plr-sym g-year gr]
;  (let [ge (get-givee-in-roster plrs-map plr-sym g-year)
;        gp (make-gift-pair (name ge) (name gr))]
;    (set-gift-pair-in-roster plrs-map plr-sym g-year gp)))
;(s/fdef set-giver-in-roster
;        :args (s/cat :plrs-map ::plr-map :plr-sym keyword?
;                     :g-year ::g-year :gr ::giver)
;        :ret ::plr-map)

(ostest/instrument)
