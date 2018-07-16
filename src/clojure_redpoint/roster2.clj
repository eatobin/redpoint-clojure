;Here is an example of The Beatles keeping track of the Xmas gifts
;they give to each other (:giver and :givee) each year over time:

(ns clojure-redpoint.roster2
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [clojure.test.check.generators :as gen]
            [clojure.spec.test.alpha :as stest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))
(s/def ::name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair :kind vector?))
(s/def :unq/player (s/keys :req-un [::name :unq/gift-history]))
(s/def ::plr-map (s/map-of keyword? :unq/player))


(defn- get-player-in-roster [plrs-map plr-sym]
  (get plrs-map plr-sym))
(s/fdef get-player-in-roster
        :args (s/cat :plrs-map ::plr-map :plr-sym keyword?)
        :ret (s/or :found :unq/player
                   :not-found nil?))

(defn- get-gift-history-in-player [plr]
  (get plr :gift-history))
(s/fdef get-gift-history-in-player
        :args (s/or :input-plr (s/cat :plr :unq/player)
                    :input-nil (s/cat :plr nil?))
        :ret (s/or :found :unq/gift-history
                   :not-found nil?))

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
                      :input-nil (s/cat :g-hist nil? :g-year any? :g-pair any?))
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

(st/instrument)

(def roster-map
  {:RinSta {:name "Ringo Starr", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
   :JohLen {:name "John Lennon", :gift-history [{:giver :JohLen, :givee :GeoHar}]},
   :GeoHar {:name "George Harrison", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
   :PauMcc {:name "Paul McCartney", :gift-history [{:giver :PauMcc, :givee :RinSta}]}})

(s/conform ::plr-map
           (set-gift-pair-in-roster roster-map :PauMcc 0 {:giver :JohLenXXX, :givee :GeoHarXXX}))
;=>
;{:RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]},
; :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
; :GeoHar {:name "George Harrison",
;          :gift-history [{:giver :PauMcc, :givee :RinSta}]},
; :PauMcc {:name "Paul McCartney",
;          :gift-history [{:giver :JohLenXXX, :givee :GeoHarXXX}]}}

;(stest/check `set-gift-pair-in-roster)
