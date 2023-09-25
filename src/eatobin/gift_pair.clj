;; $ cd Dropbox/clojure/redpoint/
;; $ clj -X:repl/socket-repl **Use this one for printing to the server REPL
;; $ bb nrepl-server 5555

;; $ clojure -M:repl/rebel
;; user=> (load-file "test/eatobin/gift_pair_test.clj")
;; user=> eatobin.gift-pair-test/gift-pair-test-gift-pair

(ns eatobin.gift-pair
  (:require [eatobin.json-utilities :refer [json-utilities-my-value-reader]]
    [eatobin.domain :as dom]
    [clojure.data.json :as json]
    [clojure.spec.alpha :as s]
    [orchestra.core :refer [defn-spec]]
    [orchestra.spec.test :as ostest]))

(defn gift-pair-json-string-to-gift-pair
  [json-string]
  (json/read-str json-string
    :value-fn json-utilities-my-value-reader
    :key-fn keyword))
(s/fdef gift-pair-json-string-to-gift-pair
  :args (s/cat :json-string ::dom/json-string)
  :ret :unq/gift-pair)

(defn-spec gift-pair-update-givee :unq/gift-pair
  [givee ::dom/givee gift-pair :unq/gift-pair]
  (assoc gift-pair :givee givee))
; (s/fdef gift-pair-update-givee
;   :args (s/cat :givee ::dom/givee
;           :gift-pair :unq/gift-pair)
;   :ret :unq/gift-pair)

(defn gift-pair-update-giver
  [giver gift-pair]
  (assoc gift-pair :giver giver))
(s/fdef gift-pair-update-giver
  :args (s/cat :giver ::dom/giver
          :gift-pair :unq/gift-pair)
  :ret :unq/gift-pair)

(ostest/instrument)
