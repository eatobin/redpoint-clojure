(defn c-it [coll] 	(conj coll {:givee :none :giver :none}))

(fn [coll] 	(conj coll {:givee :none :giver :none}))

((fn [coll] 	(conj coll {:givee :none :giver :none})) [{:givee :me :giver :you}])

#(conj % {:givee :none :giver :none})

(#(conj % {:givee :none :giver :none}) [{:givee :me :giver :you}])

(def v [[{:givee :me1 :giver :you1} {:givee :me2 :giver :you2}] [{:givee :me3 :giver :you3} {:givee :me4 :giver :you4}]])

(map c-it v)