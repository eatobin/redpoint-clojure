(ns clojure-redpoint.roster)

(get-in {:ericky
         {:name    "Eric"
          :history [[:scott :brenda] [:spot :chula]]}
         :kareny
         {:name    "Karen"
          :history [[:Sally :Maggie] [:puff :fluffy]]}}
        [:kareny :history 1 0])
