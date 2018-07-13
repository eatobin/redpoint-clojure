(ns clojure-redpoint.roster
  (:require [clojure.string :as cs]
            [clojure-csv.core :as csv]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [clojure.test.check.generators :as gen]
            [clojure.spec.test.alpha :as stest]))

(def rs "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(s/def ::roster-seq (s/coll-of vector? :kind seq?))
(s/def ::roster-info-vector (s/coll-of string? :kind vector?))
(s/def ::plrs-list (s/coll-of vector? :kind list?))
(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))
(s/def ::name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def :unq/player (s/keys :req-un [::name :unq/gift-history]))
(s/def ::plr-map-vec (s/tuple string? string? string? string?))
(s/def ::plr-map (s/map-of keyword? :unq/player))

(defn- make-roster-seq
  "Returns a lazy roster-seq"
  [roster-string]
  (let [de-spaced (cs/replace roster-string #", " ",")]
    (csv/parse-csv de-spaced)))
(s/fdef make-roster-seq
        :args (s/cat :roster-string string?)
        :ret ::roster-seq)
(s/conform ::roster-seq
           (make-roster-seq rs))
;(stest/check `make-roster-seq)

(defn- extract-roster-info-vector [roster-string]
  (first (make-roster-seq roster-string)))
(s/fdef extract-roster-info-vector
        :args (s/cat :roster-string string?)
        :ret (s/or :found ::roster-info-vector
                   :not-found nil?))
(s/conform ::roster-info-vector
           (extract-roster-info-vector rs))
(s/conform nil?
           (extract-roster-info-vector ""))
;(stest/check `extract-roster-info-vector)

(defn- extract-players-list [roster-string]
  (into () (rest (make-roster-seq roster-string))))
(s/fdef extract-players-list
        :args (s/cat :roster-string string?)
        :ret ::plrs-list)
(s/conform ::plrs-list
           (extract-players-list rs))
;(stest/check `extract-players-list)

(defn- make-gift-pair [givee giver]
  (hash-map
    :givee (keyword givee)
    :giver (keyword giver)))
(s/fdef make-gift-pair
        :args (s/cat :givee string? :giver string?)
        :ret :unq/gift-pair)
(s/conform :unq/gift-pair
           (make-gift-pair "me" "you"))
;(stest/check `make-gift-pair)

(defn- make-player [p-name g-hist]
  (hash-map
    :name p-name
    :gift-history g-hist))
(s/fdef make-player
        :args (s/cat :p-name ::name :g-hist :unq/gift-history)
        :ret :unq/player)
(s/conform :unq/player
           (make-player "us" [{:giver :me, :givee :meToo} {:givee :you :giver :youToo}]))
;(stest/check `make-player)

(defn- make-player-map [[s n ge gr]]
  (let [gp (make-gift-pair ge gr)
        plr (make-player n (vector gp))]
    (hash-map
      (keyword s) plr)))
(s/fdef make-player-map
        :args (s/cat :arg1 ::plr-map-vec)
        :ret ::plr-map)
(s/conform ::plr-map
           (make-player-map ["s" "n" "ge" "gr"]))
;(stest/check `make-player-map)

(defn- make-players-map [roster-string]
  (let [pl (extract-players-list roster-string)]
    (into {} (map make-player-map pl))))
(s/fdef make-players-map
        :args (s/cat :roster-string string?)
        :ret ::plr-map)
(s/conform ::plr-map
           (make-players-map rs))
;(stest/check `make-players-map)

(defn- get-player-in-roster [plrs-map plr-sym]
  (get plrs-map plr-sym))
(s/fdef get-player-in-roster
        :args (s/cat :plrs-map ::plr-map :plr-sym keyword?)
        :ret (s/or :found :unq/player
                   :not-found nil?))
(s/conform (s/or :found :unq/player
                 :not-found nil?)
           (get-player-in-roster {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
                                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                                  :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                                  :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}
                                 :PauMcc))
(s/conform (s/or :found :unq/player
                 :not-found nil?)
           (get-player-in-roster {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
                                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                                  :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                                  :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}}
                                 :PauMccX))
;(stest/check `get-player-in-roster) ****memory failure****

(defn- get-gift-history-in-player [plr]
  (get plr :gift-history))
(s/fdef get-gift-history-in-player
        :args (s/or :input-plr (s/cat :plr :unq/player)
                    :input-nil (s/cat :plr nil?))
        :ret (s/or :found :unq/gift-history
                   :not-found nil?))

(defn- get-gift-pair-in-gift-history [g-hist g-year]
  (get g-hist g-year))
(s/fdef get-gift-pair-in-gift-history
        :args (s/or :input-hist (s/cat :g-hist :unq/gift-history
                                       :g-year int?)
                    :input-nil (s/cat :g-hist nil?
                                      :g-year int?))
        :ret (s/or :found :unq/gift-pair
                   :not-found nil?))

(defn- get-gift-pair-in-roster [plrs-map plr-sym g-year]
  (let [plr (get-player-in-roster plrs-map plr-sym)
        gh (get-gift-history-in-player plr)]
    (get-gift-pair-in-gift-history gh g-year)))
(s/fdef get-gift-pair-in-roster
        :args (s/cat :plrs-map ::plr-map
                     :plr-sym keyword?
                     :g-year int?)
        :ret (s/or :found :unq/gift-pair
                   :not-found nil?))

(defn get-givee-in-gift-pair [g-pair]
  (get g-pair :givee))
(s/fdef get-givee-in-gift-pair
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::givee)

(defn get-giver-in-gift-pair [g-pair]
  (get g-pair :giver))
(s/fdef get-giver-in-gift-pair
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::giver)

(defn set-gift-pair-in-gift-history [g-hist g-year g-pair]
  (if (nil? g-hist)
    [{:giver :none, :givee :none}]
    (assoc g-hist g-year g-pair)))
(s/fdef set-gift-pair-in-gift-history
        :args (s/with-gen
                (s/or :input-hist (s/and
                                    (s/cat :g-hist :unq/gift-history
                                           :g-year (s/and int? #(> % -1))
                                           :g-pair :unq/gift-pair)
                                    #(<= (:g-year %) (count (:g-hist %))))
                      :input-nil (s/and
                                   (s/cat :g-hist nil?
                                          :g-year (s/and int? #(> % -1))
                                          :g-pair :unq/gift-pair)
                                   #(<= (:g-year %) (count (:g-hist %)))))
                #(gen/let [hist (s/gen :unq/gift-history)
                           year (gen/large-integer* {:min 0 :max (max 0 (dec (count hist)))})
                           pair (s/gen :unq/gift-pair)]
                   [hist year pair]))
        :ret :unq/gift-history)




(defn set-gift-history-in-player [g-hist plr]
  (if (or (nil? g-hist) (nil? plr))
    {:name "none", :gift-history [{:giver :none, :givee :none}]}
    (assoc plr :gift-history g-hist)))
(s/fdef set-gift-history-in-player
        :args (s/or :input-good (s/cat :g-hist :unq/gift-history
                                       :plr :unq/player)
                    :input-hist-nil (s/cat :g-hist nil?
                                           :plr :unq/player)
                    :input-plr-nil (s/cat :g-hist :unq/gift-history
                                          :plr nil?)
                    :input-both-nil (s/cat :g-hist nil?
                                           :plr nil?))
        :ret :unq/player)

(defn set-gift-pair-in-roster [plrs-map plr-sym g-year g-pair]
  (let [plr (get-player-in-roster plrs-map plr-sym)
        gh (get-gift-history-in-player plr)
        ngh (set-gift-pair-in-gift-history gh g-year g-pair)
        nplr (set-gift-history-in-player ngh plr)]
    (assoc plrs-map plr-sym nplr)))
(s/fdef set-gift-pair-in-roster
        :args (s/cat :plrs-map ::plr-map
                     :plr-sym keyword?
                     :g-year (s/and int? #(> % -1))
                     :g-pair :unq/gift-pair)
        :ret ::plr-map)



; (def keyword-vector (gen/such-that not-empty (gen/vector gen/keyword)))

; (def vec-and-elem
;   (gen/bind keyword-vector
;             (fn [v] (gen/tuple (gen/elements v) (gen/return v)))))

; (gen/sample vec-and-elem 9)

; (def hist (s/gen :unq/gift-history))

; (def year
;   (gen/bind hist
;             (fn [v] (gen/large-integer* {:min 0 :max (max 0 (dec (count v)))}))))

; (def pair (s/gen :unq/gift-pair))

;(s/fdef set-gift-pair-in-gift-history
;        :args (s/cat :g-hist :unq/gift-history
;                     :g-year int?
;                     :g-pair :unq/gift-pair)
;        :ret :unq/gift-history)

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

(st/instrument)


;(s/conform vector?
;           (extract-roster-info-vector rs))
;(s/conform nil?
;           (extract-roster-info-vector ""))
;(s/conform :unq/gift-pair
;           (make-gift-pair "one" "two"))
;(s/conform ::plrs-list
;           (extract-players-list rs))
;(s/conform ::plrs-list
;           (extract-players-list ""))
;(def x (make-gift-pair "joe" "bob"))
;(def y (make-gift-pair "joey" "bobby"))
;(def h [x y])
;(s/conform :unq/gift-history h)
;(s/conform :unq/player
;           (make-player "eric" h))
;(s/conform ::plr-map (make-player-map ["s" "n" "ge" "gr"]))
;(def pm (make-players-map rs))
;(s/conform (s/or :found :unq/player
;                 :not-found nil?)
;           (get-player-in-roster pm :GeoHar))
;; =>
;; [:found
;;  {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]}]
;(s/conform (s/or :found :unq/player
;                 :not-found nil?)
;           (get-player-in-roster pm :GeoHarX))
;; => [:not-found nil]
;(s/conform ::givee
;           (get-givee-in-gift-pair {:giver :PauMcc, :givee :RinSta}))
;(s/conform :unq/player
;           (set-gift-history-in-player [{:giver :RinSta, :givee :PauMcc}] {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}))
;(stest/check `set-gift-pair-in-gift-history)




;(stest/check `set-gift-pair-in-roster)
;=>
;({:spec #object[clojure.spec.alpha$fspec_impl$reify__2451
;                0x7f3251fa
;                "clojure.spec.alpha$fspec_impl$reify__2451@7f3251fa"],
;  :clojure.spec.test.check/ret {:result #error{:cause "Call to #'clojure-redpoint.roster/set-gift-pair-in-gift-history did not conform to spec:
;                                                       In: [0] val: nil fails spec: :unq/gift-history at: [:args :input-hist :g-hist] predicate: vector?
;                                                       val: {:g-hist nil, :g-year 43, :g-pair {:givee :t, :giver :h}} fails at: [:args :input-nil] predicate: (<= (:g-year %) (count (:g-hist %)))
;                                                       ",
;                                               :data {:clojure.spec.alpha/problems ({:path [:args
;                                                                                            :input-hist
;                                                                                            :g-hist],
;                                                                                     :pred clojure.core/vector?,
;                                                                                     :val nil,
;                                                                                     :via [:unq/gift-history
;                                                                                           :unq/gift-history],
;                                                                                     :in [0]}
;                                                                                     {:path [:args
;                                                                                             :input-nil],
;                                                                                      :pred (clojure.core/fn
;                                                                                              [%]
;                                                                                              (clojure.core/<=
;                                                                                                (:g-year
;                                                                                                  %)
;                                                                                                (clojure.core/count
;                                                                                                  (:g-hist
;                                                                                                    %)))),
;                                                                                      :val {:g-hist nil,
;                                                                                            :g-year 43,
;                                                                                            :g-pair {:givee :t,
;                                                                                                     :giver :h}},
;                                                                                      :via [],
;                                                                                      :in []}),
;                                                      :clojure.spec.alpha/spec #object[clojure.spec.alpha$or_spec_impl$reify__2046
;                                                                                       0x46e540de
;                                                                                       "clojure.spec.alpha$or_spec_impl$reify__2046@46e540de"],
;                                                      :clojure.spec.alpha/value (nil
;                                                                                  43
;                                                                                  {:givee :t,
;                                                                                   :giver :h}),
;                                                      :clojure.spec.alpha/args (nil
;                                                                                 43
;                                                                                 {:givee :t,
;                                                                                  :giver :h}),
;                                                      :clojure.spec.alpha/failure :instrument,
;                                                      :orchestra.spec.test/caller {:file "roster.clj",
;                                                                                   :line 162,
;                                                                                   :var-scope clojure-redpoint.roster/set-gift-pair-in-roster}},
(def roster-map
  {:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
   :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
   :GeoHar {:name "George Harrison", :gift-history [{:giver :PauMcc, :givee :RinSta}]},
   :PauMcc {:name "Paul McCartney", :gift-history [{:giver :JohLen, :givee :GeoHar}]}})
