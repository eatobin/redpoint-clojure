(ns clojure-redpoint.roster
  (:require [clojure-csv.core :as csv]))

;"blackhawks2010.txt"
;"roster-test.txt"

(def team-name)
(def first-year)
(def roster (atom {}))

(defn make-map [v]
  (if (= 4 (count v))
    (let [[s n ge gr] v]
      (hash-map (keyword s)
                (hash-map :name n
                          :gift-history (vector (hash-map
                                                  :givee (keyword ge)
                                                  :giver (keyword gr))))))))

(defn make-roster [f]
  (let [slurped (slurp f)
        de-spaced (clojure.string/replace slurped #", " ",")
        parsed (csv/parse-csv de-spaced)]
    (def team-name ((first (vec parsed)) 0))
    (def first-year (read-string ((first (vec parsed)) 1)))
    (reset! roster
            (into {} (map make-map parsed)))))

(defn get-player-name [p]
  (get-in (deref roster)
          [p :name]))

(defn get-givee-code [p y]
  (get-in (deref roster)
          [p :gift-history y :givee]))

(defn set-givee-code [p y ge]
  (if (and (contains? (deref roster) p)
           (<= (+ y 1) (count
                         (get-in (deref roster)
                                 [p :gift-history]))))
    (let [gr (get-in (deref roster)
                     [p :gift-history y :giver])]
      (swap! roster assoc-in
             [p :gift-history y]
             {:giver gr :givee ge}))))

(defn get-giver-code [p y]
  (get-in (deref roster)
          [p :gift-history y :giver]))

(defn set-giver-code [p y gr]
  (if (and (contains? (deref roster) p)
           (<= (+ y 1) (count
                         (get-in (deref roster)
                                 [p :gift-history]))))
    (let [ge (get-in (deref roster)
                     [p :gift-history y :givee])]
      (swap! roster assoc-in
             [p :gift-history y]
             {:giver gr :givee ge}))))

(defn add-new-year []
  (doseq [p (keys (deref roster))]
    (swap! roster update-in
           [p :gift-history]
           conj
           {:givee :none :giver :none})))

;def print_giving_roster(gift_year)
;no_givee = Array.new
;no_giver = Array.new
;
;puts @team_name + ' - Year ' + (@first_year.to_i + gift_year).to_s + ' Gifts:'
;
;@roster_list.keys.sort.each do |player_code|
;player_name = get_player(player_code).player_name
;# player_name = get_player_name(player_code)
;givee_code = get_givee_code(player_code, gift_year)
;giver_code = get_giver_code(player_code, gift_year)
;
;if givee_code.equal?(:none)
;no_givee << player_code
;else
;puts player_name + ' is buying for ' + get_player(givee_code).player_name
;end
;if giver_code.equal?(:none)
;no_giver << player_code
;end
;end

(def r (atom []))
(swap! r conj "Test 1 ")
(swap! r conj "and test 2")
(apply str (deref r))

(defn print-giving-roster [gift-year]
  (def no-givee [])
  (def no-giver [])
  (def roster-string (atom []))
  (swap! roster-string conj team-name " - Year " (+ first-year gift-year) " Gifts:\n"))
