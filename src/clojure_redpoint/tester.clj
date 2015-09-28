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

(defn postwalk-mapentry
  [smap nmap form]
  (postwalk (fn [coll] (conj coll {:givee :none :giver :none})) form))


(defn postwalk-mapentry
  [form]
  (postwalk (fn [x] (if (= :gift-history x) #(conj % {:givee :none :giver :none}))) form))

(defn prewalk-mapentry
  [form]
  (prewalk (fn [x] (if (= 1 1) (println x))) form))

(defn prewalk-mapentry
  [form]
  (prewalk (fn [x] (if (= x :gift-history) (println x))) form))

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

(doseq [x roster-list] (println x))

(doseq [x roster-list
               :when (= x :gift-history)]
         (println x))

(def config [{:host "test", :port 1},{:host "testtest", :port 2}])

(for [{h :host p :port} config]
  (format "host: %s ; port: %s" h p))

(def config [{:host "test", :port 1},{:host "testtest", :port 2}])

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

(def roster-list2 {:TroBro
                  {:name "Troy Brouwer"
                   :gift-history
                         [{:givee :DavBol
                           :giver :JoeQue}]}
                  :JoeQue
                  {:name "Joel Quenneville"
                   :gift-history
                         [{:givee :TroBro
                           :giver :AndLad}]}})

[{:sym :Trobro, :name "Troy Brouwer", :gift-history [{:givee :DavBol :giver :JoeQue}]}
 {:sym :JoeQue, :name "Joel Quenneville", :gift-history [{:givee :TroBro :giver :AndLad}]}]

{:TroBro {:name "Troy Brouwer" :gift-history [{:givee :DavBol :giver :JoeQue}]},
 :JoeQue {:name "Joel Quenneville" :gift-history [{:givee :TroBro :giver :AndLad}]}}
