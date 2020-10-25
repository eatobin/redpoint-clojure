(ns redpoint.roster
  (:require [redpoint.domain :as dom]
            [clojure.data.json :as json]
            [redpoint.players :as plrs]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

;; (defrecord Roster [roster-name roster-year players])
;; (s/fdef ->Roster
;;         :args (s/cat :roster-name ::dom/roster-name
;;                      :roster-year ::dom/roster-year
;;                      :players ::dom/players)
;;         :ret ::dom/roster)

(defn- my-key-reader
  [key]
  (cond
    (= key "playerName") :player-name
    (= key "giftHistory") :gift-history
    (= key "rosterName") :roster-name
    (= key "rosterYear") :roster-year
    :else (keyword key)))

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

(defn roster-json-string-to-Roster [json-string]
  (json/read-str json-string
                 :value-fn my-value-reader
                 :key-fn my-key-reader))
(s/fdef roster-json-string-to-Roster
  :args (s/cat :json-string string?)
  :ret :unq/roster)

(ostest/instrument)
