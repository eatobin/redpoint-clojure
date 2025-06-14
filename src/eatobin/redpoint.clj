;; [eric@linux-x2vq redpoint]$ clojure -M:run-m

(ns eatobin.redpoint
  (:require [clojure.string :as string]
            [eatobin.my-state :refer [my-state-ask-continue
                                      my-state-json-string-to-my-state
                                      my-state-print-results
                                      my-state-update-and-run-new-year]])
  (:gen-class))

(def hawks-json "{
  \"rosterName\": \"Blackhawks\",
  \"rosterYear\": 2010,
  \"players\": {
    \"TroBro\": {
      \"playerName\": \"Troy Brouwer\",
      \"giftHistory\": [
        {
          \"givee\": \"DavBol\",
          \"giver\": \"JoeQue\"
        }
      ]
    },
    \"JoeQue\": {
      \"playerName\": \"Joel Quenneville\",
      \"giftHistory\": [
        {
          \"givee\": \"TroBro\",
          \"giver\": \"AndLad\"
        }
      ]
    },
    \"AdaBur\": {
      \"playerName\": \"Adam Burish\",
      \"giftHistory\": [
        {
          \"givee\": \"DunKei\",
          \"giver\": \"JonToe\"
        }
      ]
    },
    \"AndLad\": {
      \"playerName\": \"Andrew Ladd\",
      \"giftHistory\": [
        {
          \"givee\": \"JoeQue\",
          \"giver\": \"KriVer\"
        }
      ]
    },
    \"AntNie\": {
      \"playerName\": \"Antti Niemi\",
      \"giftHistory\": [
        {
          \"givee\": \"JonToe\",
          \"giver\": \"MarHos\"
        }
      ]
    },
    \"BreSea\": {
      \"playerName\": \"Brent Seabrook\",
      \"giftHistory\": [
        {
          \"givee\": \"KriVer\",
          \"giver\": \"NikHja\"
        }
      ]
    },
    \"BryBic\": {
      \"playerName\": \"Bryan Bickell\",
      \"giftHistory\": [
        {
          \"givee\": \"MarHos\",
          \"giver\": \"PatKan\"
        }
      ]
    },
    \"CriHue\": {
      \"playerName\": \"Cristobal Huet\",
      \"giftHistory\": [
        {
          \"givee\": \"PatKan\",
          \"giver\": \"TomKop\"
        }
      ]
    },
    \"DavBol\": {
      \"playerName\": \"Dave Bolland\",
      \"giftHistory\": [
        {
          \"givee\": \"PatSha\",
          \"giver\": \"TroBro\"
        }
      ]
    },
    \"DunKei\": {
      \"playerName\": \"Duncan Keith\",
      \"giftHistory\": [
        {
          \"givee\": \"TomKop\",
          \"giver\": \"AdaBur\"
        }
      ]
    },
    \"JonToe\": {
      \"playerName\": \"Jonathan Toews\",
      \"giftHistory\": [
        {
          \"givee\": \"AdaBur\",
          \"giver\": \"AntNie\"
        }
      ]
    },
    \"KriVer\": {
      \"playerName\": \"Kris Versteeg\",
      \"giftHistory\": [
        {
          \"givee\": \"AndLad\",
          \"giver\": \"BreSea\"
        }
      ]
    },
    \"MarHos\": {
      \"playerName\": \"Marian Hossa\",
      \"giftHistory\": [
        {
          \"givee\": \"AntNie\",
          \"giver\": \"BryBic\"
        }
      ]
    },
    \"NikHja\": {
      \"playerName\": \"Niklas Hjalmarsson\",
      \"giftHistory\": [
        {
          \"givee\": \"BreSea\",
          \"giver\": \"BriCam\"
        }
      ]
    },
    \"PatKan\": {
      \"playerName\": \"Patrick Kane\",
      \"giftHistory\": [
        {
          \"givee\": \"BryBic\",
          \"giver\": \"CriHue\"
        }
      ]
    },
    \"PatSha\": {
      \"playerName\": \"Patrick Sharp\",
      \"giftHistory\": [
        {
          \"givee\": \"BriCam\",
          \"giver\": \"DavBol\"
        }
      ]
    },
    \"TomKop\": {
      \"playerName\": \"Tomas Kopecky\",
      \"giftHistory\": [
        {
          \"givee\": \"CriHue\",
          \"giver\": \"DunKei\"
        }
      ]
    },
    \"BriCam\": {
      \"playerName\": \"Brian Campbell\",
      \"giftHistory\": [
        {
          \"givee\": \"NikHja\",
          \"giver\": \"PatSha\"
        }
      ]
    }
  },
  \"gift-year\":0,
  \"givee-hat\":[],
  \"giver-hat\":[],
  \"maybe-givee\":null,
  \"maybe-giver\":null,
  \"discards\":[],
  \"quit\":\"n\"}")

(def first-state
  (->
    hawks-json
    (my-state-json-string-to-my-state)))

(defn -main
  []
  (loop [next-state (my-state-ask-continue (my-state-print-results first-state))]
    (if (= (string/lower-case (:quit next-state)) "q")
      (do
        (println)
        (println "This was fun!")
        (println "Talk about a position with Redpoint?")
        (println "Please call: Eric Tobin 773-679-6617")
        (println))
      (recur
        (->
          next-state
          (my-state-update-and-run-new-year)
          (my-state-print-results)
          (my-state-ask-continue))))))
