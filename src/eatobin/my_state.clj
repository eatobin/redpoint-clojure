(ns eatobin.my-state
  (:require [clojure.data.json :as json]
            [eatobin.domain :as dom]
            [eatobin.hat :refer [hat-make-hat
                                 hat-remove-puck
                                 hat-discard-givee
                                 hat-return-discards]]
            [eatobin.json-utilities :refer [json-utilities-my-value-reader
                                            json-utilities-my-key-reader]]
            [eatobin.players :refer [players-add-year
                                     players-update-my-givee
                                     players-update-my-giver
                                     players-get-my-givee
                                     players-get-my-giver
                                     players-get-player-name]]
            [eatobin.rules :refer [rules-givee-not-self?
                                   rules-givee-not-recip?
                                   rules-givee-not-repeat?]]
            [orchestra.core :refer [defn-spec]]
            [orchestra.spec.test :as ostest]))

(defn-spec my-state-json-string-to-my-state :unq/my-state
  [json-string ::dom/json-string]
  (json/read-str json-string
                 :value-fn json-utilities-my-value-reader
                 :key-fn json-utilities-my-key-reader))

(defn-spec my-state-draw-puck ::dom/maybe-player-key
  [hat :unq/hat]
  (when (seq hat)
    ((shuffle hat) 0)))

(defn-spec my-state-start-new-year :unq/my-state
  [state :unq/my-state]
  (let [fresh-hat (hat-make-hat (:players state))]
    (assoc state
           :gift-year (inc (:gift-year state))
           :players (players-add-year (:players state))
           :givee-hat fresh-hat
           :giver-hat fresh-hat
           :maybe-givee (my-state-draw-puck fresh-hat)
           :maybe-giver (my-state-draw-puck fresh-hat)
           :discards #{})))

(defn-spec my-state-givee-is-failure :unq/my-state
  [state :unq/my-state]
  (let [givee-to-remove (:maybe-givee state)
        diminished-givee-hat (hat-remove-puck givee-to-remove (:givee-hat state))]
    (assoc state
           :givee-hat diminished-givee-hat
           :maybe-givee (my-state-draw-puck diminished-givee-hat)
           :discards (hat-discard-givee givee-to-remove (:discards state)))))

(defn-spec my-state-givee-is-success :unq/my-state
  [state :unq/my-state]
  (let [current-giver (:maybe-giver state)
        current-givee (:maybe-givee state)
        updated-givee-players (players-update-my-givee current-giver current-givee (:gift-year state) (:players state))]
    (assoc state
           :players (players-update-my-giver current-givee current-giver (:gift-year state) updated-givee-players)
           :givee-hat (hat-remove-puck current-givee (:givee-hat state))
           :maybe-givee nil)))

(defn-spec my-state-select-new-giver :unq/my-state
  [state :unq/my-state]
  (let [giver-to-remove (:maybe-giver state)
        replenished-givee-hat (hat-return-discards (:discards state) (:givee-hat state))
        diminished-giver-hat (hat-remove-puck giver-to-remove (:giver-hat state))]
    (assoc state
           :givee-hat replenished-givee-hat
           :giver-hat diminished-giver-hat
           :maybe-givee (my-state-draw-puck replenished-givee-hat)
           :maybe-giver (my-state-draw-puck diminished-giver-hat)
           :discards #{})))

(defn-spec my-state-errors :unq/error-seq
  [state :unq/my-state]
  (let [the-players (:players state)
        the-year (:gift-year state)]
    (seq (for [plr-sym (keys (into (sorted-map) the-players))
               :let [giver-code (players-get-my-giver plr-sym the-players the-year)
                     givee-code (players-get-my-givee plr-sym the-players the-year)]
               :when (or (= plr-sym giver-code) (= plr-sym givee-code))]
           plr-sym))))

(defn-spec my-state-print-results :unq/my-state
  [state :unq/my-state]
  (let [the-players (:players state)
        the-year (:gift-year state)
        the-roster-name (:roster-name state)
        the-roster-year (:roster-year state)]
    (println)
    (println the-roster-name "- Year" (+ the-roster-year the-year) "Gifts:")
    (println)
    (doseq [plr-sym (keys (into (sorted-map) the-players))
            :let [player-name (players-get-player-name plr-sym the-players)
                  givee-code (players-get-my-givee plr-sym the-players the-year)
                  givee-name (players-get-player-name givee-code the-players)
                  giver-code (players-get-my-giver plr-sym the-players the-year)]]
      (cond
        (and (= plr-sym givee-code) (= plr-sym giver-code)) (println player-name "is neither **buying** for nor **receiving** from anyone - **ERROR**")
        (= plr-sym giver-code) (println player-name "is **receiving** from no one - **ERROR**")
        (= plr-sym givee-code) (println player-name "is **buying** for no one - **ERROR**")
        :else (println player-name "is buying for" givee-name)))
    (when (my-state-errors state)
      (println)
      (println "There is a logic error in this year's pairings.")
      (println "Do you see it?")
      (println "If not... call me and I'll explain!")))
  state)

(defn-spec my-state-ask-continue :unq/my-state
  [state :unq/my-state]
  (println)
  (print "Continue? ('q' to quit): ")
  (flush)
  (assoc state
         :quit (read-line)))

(defn-spec my-state-loop :unq/my-state
  [altered-state :unq/my-state]
  (let [the-maybe-giver (:maybe-giver altered-state)
        the-maybe-givee (:maybe-givee altered-state)
        the-gift-year (:gift-year altered-state)
        the-players (:players altered-state)]
    (if (some? the-maybe-giver)
      (if (some? the-maybe-givee)
        (if (and (rules-givee-not-self? the-maybe-giver the-maybe-givee)
                 (rules-givee-not-recip? the-maybe-giver the-maybe-givee the-gift-year the-players)
                 (rules-givee-not-repeat? the-maybe-giver the-maybe-givee the-gift-year the-players))
          (recur (my-state-givee-is-success altered-state))
          (recur (my-state-givee-is-failure altered-state)))
        (recur (my-state-select-new-giver altered-state)))
      altered-state)))

(defn-spec my-state-update-and-run-new-year :unq/my-state
  [state :unq/my-state]
  (let [new-year-state (my-state-start-new-year state)]
    (my-state-loop new-year-state)))

(ostest/instrument)
