;; $ clj -X:repl/socket-repl:eat/orchestra:eat/test
;; **Use this one for printing to the server REPL

;; $ clojure -M:repl/rebel
;; user=> (load-file "test/eatobin/gift_pair_test.clj")
;; user=> eatobin.gift-pair-test/gift-pair-test-gift-pair

(ns eatobin.gift-pair
  (:require
    [clojure.data.json :as json]
    [clojure.spec.alpha :as s]
    [eatobin.domain :as dom]
    [eatobin.json-utilities :refer [json-utilities-my-value-reader]]))

(defn gift-pair-json-string-to-gift-pair
  [json-string]
  (json/read-str json-string
                 :value-fn json-utilities-my-value-reader
                 :key-fn keyword))
(s/fdef gift-pair-json-string-to-gift-pair
        :args (s/cat :json-string ::dom/json-string)
        :ret :unq/gift-pair)

(defn gift-pair-update-givee
  [givee gift-pair]
  (assoc gift-pair :givee givee))
(s/fdef gift-pair-update-givee
        :args (s/cat :givee ::dom/givee
                     :gift-pair :unq/gift-pair)
        :ret :unq/gift-pair)

(defn gift-pair-update-giver
  [giver gift-pair]
  (assoc gift-pair :giver giver))
(s/fdef gift-pair-update-giver
        :args (s/cat :giver ::dom/giver
                     :gift-pair :unq/gift-pair)
        :ret :unq/gift-pair)
