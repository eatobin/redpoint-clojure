(ns clojure-redpoint.roster)

(defn c-it [coll] 	(conj coll {:givee :none :giver :none}))

(fn [coll] (conj coll {:givee :none :giver :none}))

((fn [coll] (conj coll {:givee :none :giver :none})) [{:givee :me :giver :you}])

#(conj % {:givee :none :giver :none})

(#(conj % {:givee :none :giver :none}) [{:givee :me :giver :you}])

(def v [[{:givee :me1 :giver :you1} {:givee :me2 :giver :you2}] [{:givee :me3 :giver :you3} {:givee :me4 :giver :you4}]])

(map c-it v)

(#(conj % {:givee :none :giver :none}) (get-in (deref roster)
                                               [:AdaBur :gift-history]))

(assoc-in @roster [:AdaBur :gift-history] {:givee :none :giver :none})

(def roster-list {:TroBro
                  {:name "Troy Brouwer"
                   :gift-history
                         [{:givee :DavBol
                           :giver :JoeQue}]}
                  :JoeQue
                  {:name "Joel Quenneville"
                   :gift-history
                         [{:givee :TroBro
                           :giver :AndLad}]}})

(doseq [[k v] roster-list] (println [k (:gift-history v)]))

(def book {:name "SICP" :details {:pages 657 :isbn-10 "0262011530"}})
(let [{name :name {pages :pages isbn-10 :isbn-10} :details} book]
  (println "name:" name "pages:" pages "isbn-10:" isbn-10))

(def roster-list {:name "Troy Brouwer" :gift-history [{:givee :DavBol :giver :JoeQue}]})
(let [{name :name [{givee :givee giver :giver}] :gift-history} roster-list]
  (println "name:" name "gift-history" "[givee" givee "giver" giver "]"))

(for [{gh :gift-history} roster-list]
  (#(conj % {:givee :none :giver :none}) gh))

(for [{p {n :name}} roster-list]
  (clojure.string/upper-case n))

(def me {:name
         {:firstname "John"
          :middlename "Lawrence"
          :surname "Aspden"}
         :address
         {:street "Catherine Street"
          :town {:name "Cambridge"
                 :county "Cambridgeshire"
                 :country{
                          :name "England"
                          :history "Faded Imperial Power"
                          :role-in-world "Beating Australia at Cricket"}}}})


(:name me)
(get me :name)

(get-in me [:name :middlename])
(reduce get me [:address :town :country :role-in-world])
(-> me :address :town :county)

(assoc-in me [:name :initials] "JLA")
(update-in me [:address :street] #(str "33 " %))

[{:sym :Trobro, :name "Troy Brouwer", :gift-history [{:givee :DavBol :giver :JoeQue}]}
 {:sym :JoeQue, :name "Joel Quenneville", :gift-history [{:givee :TroBro :giver :AndLad}]}]

{:TroBro {:name "Troy Brouwer" :gift-history [{:givee :DavBol :giver :JoeQue}]},
 :JoeQue {:name "Joel Quenneville" :gift-history [{:givee :TroBro :giver :AndLad}]}}

;; prepare a seq of all keys from entries whose values are 0
(for [[x y] '([:a 1] [:b 2] [:c 0]) :when (= y 0)] x)
;;=> (:c)

(for [[k v] roster-list] (conj (:gift-history v) {:givee :none :giver :none}))

(for [[k v] roster-list
      :let [n (conj (:gift-history v) {:givee :none :giver :none})]]
  n)

(for [[k v] roster-list
      :let [n (conj (:gift-history v) {:givee :none :giver :none})]]
  [k n])

(for [[k v] roster-list
      :let [n (conj (:gift-history v) {:givee :none :giver :none})
            nv (assoc-in roster-list [k :gift-history] n)]]
  [k nv])
