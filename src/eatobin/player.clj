(ns eatobin.player
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [eatobin.domain :as dom]
            [eatobin.json-utilities :refer [json-utilities-my-key-reader
                                            json-utilities-my-value-reader]]))

(defn player-json-string-to-player
  [json-string]
  (json/read-str json-string
                 :value-fn json-utilities-my-value-reader
                 :key-fn json-utilities-my-key-reader))
(s/fdef player-json-string-to-player
        :args (s/cat :json-string ::dom/json-string)
        :ret :unq/player)

(defn player-update-gift-history
  "Sets a gift history into the provided player"
  [gift-history player]
  (assoc player :gift-history gift-history))
(s/fdef player-update-gift-history
        :args (s/cat :gift-history :unq/gift-history
                     :player :unq/player)
        :ret :unq/player)
