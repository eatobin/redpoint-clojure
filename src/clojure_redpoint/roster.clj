(ns clojure-redpoint.roster
  (:require [clojure.string :as cs]
            [clojure-csv.core :as csv]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

(s/def ::roster-seq (s/coll-of vector?))
(s/def ::plrs-list (s/coll-of vector?))
(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))
(s/def ::name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair))
(s/def :unq/player (s/keys :req-un [::name :unq/gift-history]))
;(s/def ::plr-sym keyword?)
(s/def :unq/plr-map (s/keys :req-un [::plr-sym :unq/player]))
(s/def ::bindings (s/and vector? (s/* ::binding)))
(s/def ::binding (s/cat :binding ::binding-form
                        :init-expr any?))
(s/def ::binding-form simple-symbol?)

(defn- make-roster-seq
  "Returns a lazy roster-seq"
  [roster-string]
  (let [de-spaced (cs/replace roster-string #", " ",")]
    (csv/parse-csv de-spaced)))

(s/fdef make-roster-seq
        :args (s/cat :roster-string string?)
        :ret ::roster-seq)

(defn- extract-roster-info-vector [roster-string]
  (first (make-roster-seq roster-string)))

(s/fdef extract-roster-info-vector
        :args (s/cat :roster-string string?)
        :ret (s/or :found vector?
                   :not-found nil?))

(defn- extract-players-list [roster-string]
  (into () (rest (make-roster-seq roster-string))))

(s/fdef extract-players-list
        :args (s/cat :roster-string string?)
        :ret ::plrs-list)

(defn- make-gift-pair [givee giver]
  (hash-map
    :givee (keyword givee)
    :giver (keyword giver)))

(s/fdef make-gift-pair
        :args (s/cat :givee string? :giver string?)
        :ret :unq/gift-pair)

(defn- make-player [p-name g-hist]
  (hash-map
    :name p-name
    :gift-history g-hist))

(s/fdef make-player
        :args (s/cat :p-name ::name :g-hist :unq/gift-history)
        :ret :unq/player)

(defn- make-player-map [[s n ge gr]]
  (let [gp (make-gift-pair ge gr)
        plr (make-player n (vector gp))]
    (hash-map
      (keyword s) plr)))

(s/fdef make-player-map
        :args [(s/cat :s string?)
               (s/cat :n string?)
               (s/cat :ge string?)
               (s/cat :gr string?)]
        :ret :unq/plr-map)

;(defn get-roster-name [roster-list]
;  (let [line (extract-roster-info-vector roster-list)]
;    (first line)))
;
;(defn get-roster-year [roster-list]
;  (let [line (extract-roster-info-vector roster-list)]
;    (read-string (last line))))
;
;(defn get-player-name-in-roster [plrs-map plr-sym]
;  (let [plr (get-player-in-roster plrs-map plr-sym)]
;    (get plr :name)))
;
;(defn get-givee-in-roster [plrs-map plr-sym g-year]
;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;    (get-givee-in-gift-pair gp)))
;
;(defn get-giver-in-roster [plrs-map plr-sym g-year]
;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;    (get-giver-in-gift-pair gp)))
;
;(defn set-givee-in-roster [plrs-map plr-sym g-year ge]
;  (if (check-give plrs-map plr-sym g-year ge)
;    (let [gr (get-giver-in-roster plrs-map plr-sym g-year)
;          gp (make-gift-pair ge gr)]
;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;    plrs-map))
;
;(defn set-giver-in-roster [plrs-map plr-sym g-year gr]
;  (if (check-give plrs-map plr-sym g-year gr)
;    (let [ge (get-givee-in-roster plrs-map plr-sym g-year)
;          gp (make-gift-pair ge gr)]
;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;    plrs-map))

(stest/instrument)
