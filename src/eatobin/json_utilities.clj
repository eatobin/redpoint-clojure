(ns eatobin.json-utilities)

(defn json-utilities-my-value-reader
  [key value]
  (cond
    (= key :givee) (keyword value)
    (= key :giver) (keyword value)
    (= key :discards) (into #{} value)
    (= key :givee-hat) (into #{} value)
    (= key :giver-hat) (into #{} value)
    :else value))

(defn json-utilities-my-key-reader
  [key]
  (cond
    (= key "playerName") :player-name
    (= key "giftHistory") :gift-history
    (= key "rosterName") :roster-name
    (= key "rosterYear") :roster-year
    :else (keyword key)))
