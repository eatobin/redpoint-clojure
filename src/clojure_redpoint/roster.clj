(ns clojure-redpoint.roster
  (:require [clojure-csv.core :as csv]))

;"blackhawks2010.txt"
;"roster-test.txt"

(def roster (atom {}))

(defn make-map [v]
  (if (= 2 (count v))
    (let [[tn fy] v]
      (hash-map :team-name tn :first-year (read-string fy)))
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
    (reset! roster
            (into {} (map make-map parsed)))))

(defn get-player-name [p]
  (get-in (deref roster)
          [p :name]))

(defn get-givee-code [p y]
  (get-in (deref roster)
          [p :gift-history y :givee]))

;(defn set-givee-code [p ge y]
;  TODO: code this as below
;  )

(defn add-history [p y ge gr]
  (swap! roster assoc-in
         [p :gift-history y]
         {:givee ge :giver gr}))



;def set_givee_code(player_code, setee_code, gift_year)
;if get_player(player_code).nil? || get_player(setee_code).nil?
;nil
;else
;roles = get_player(player_code).gift_history[gift_year]
;roles[:givee] = setee_code
;get_player(player_code).gift_history[gift_year] = roles
;get_player(player_code).gift_history[gift_year][:givee]
;end
;end
