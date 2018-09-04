;(ns clojure-redpoint.roster
;  (:require
;    [clojure.spec.alpha :as s]
;    [orchestra.spec.test :as st]
;    [clojure.test.check.generators :as gen]))
;
;
;
;(defn set-gift-pair-in-roster [plrs-map plr-sym g-year g-pair]
;  (let [plr (get-player-in-roster plrs-map plr-sym)
;        gh (get-gift-history-in-player plr)
;        ngh (set-gift-pair-in-gift-history gh g-year g-pair)
;        nplr (set-gift-history-in-player ngh plr)]
;    (assoc plrs-map plr-sym nplr)))
;(s/fdef set-gift-pair-in-roster
;        :args (s/cat :plrs-map ::plr-map
;                     :plr-sym keyword?
;                     :g-year (s/and int? #(> % -1))
;                     :g-pair :unq/gift-pair)
;        :ret ::plr-map)
;
;
;
;; (def keyword-vector (gen/such-that not-empty (gen/vector gen/keyword)))
;
;; (def vec-and-elem
;;   (gen/bind keyword-vector
;;             (fn [v] (gen/tuple (gen/elements v) (gen/return v)))))
;
;; (gen/sample vec-and-elem 9)
;
;; (def hist (s/gen :unq/gift-history))
;
;; (def year
;;   (gen/bind hist
;;             (fn [v] (gen/large-integer* {:min 0 :max (max 0 (dec (count v)))}))))
;
;; (def pair (s/gen :unq/gift-pair))
;
;;(s/fdef set-gift-pair-in-gift-history
;;        :args (s/cat :g-hist :unq/gift-history
;;                     :g-year int?
;;                     :g-pair :unq/gift-pair)
;;        :ret :unq/gift-history)
;
;;(defn get-roster-name [roster-list]
;;  (let [line (extract-roster-info-vector roster-list)]
;;    (first line)))
;;
;;(defn get-roster-year [roster-list]
;;  (let [line (extract-roster-info-vector roster-list)]
;;    (read-string (last line))))
;;
;;(defn get-player-name-in-roster [plrs-map plr-sym]
;;  (let [plr (get-player-in-roster plrs-map plr-sym)]
;;    (get plr :name)))
;;
;;(defn get-givee-in-roster [plrs-map plr-sym g-year]
;;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;;    (get-givee-in-gift-pair gp)))
;;
;;(defn get-giver-in-roster [plrs-map plr-sym g-year]
;;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;;    (get-giver-in-gift-pair gp)))
;;
;;(defn set-givee-in-roster [plrs-map plr-sym g-year ge]
;;  (if (check-give plrs-map plr-sym g-year ge)
;;    (let [gr (get-giver-in-roster plrs-map plr-sym g-year)
;;          gp (make-gift-pair ge gr)]
;;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;;    plrs-map))
;;
;;(defn set-giver-in-roster [plrs-map plr-sym g-year gr]
;;  (if (check-give plrs-map plr-sym g-year gr)
;;    (let [ge (get-givee-in-roster plrs-map plr-sym g-year)
;;          gp (make-gift-pair ge gr)]
;;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;;    plrs-map))
;
;(st/instrument)
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
