(gen/sample
  (gen/let [hist (gen/frequency [[5 (s/gen :unq/gift-history)] [5 (gen/return nil)]])
            year (gen/large-integer* {:min 0 :max (max 0 (dec (count hist)))})
            pair (s/gen :unq/gift-pair)]
           [hist year pair]) 3)


(s/fdef set-gift-pair-in-gift-history
        :args (s/with-gen
                (s/or :input-hist (s/and
                                    (s/cat :g-hist :unq/gift-history
                                           :g-year (s/and int? #(> % -1))
                                           :g-pair :unq/gift-pair)
                                    #(<= (:g-year %) (count (:g-hist %))))
                      :input-nil (s/and
                                   (s/cat :g-hist nil?
                                          :g-year (s/and int? #(> % -1))
                                          :g-pair :unq/gift-pair)
                                   #(<= (:g-year %) (count (:g-hist %)))))
                #(gen/let [hist (s/gen :unq/gift-history)
                           year (gen/large-integer* {:min 0 :max (max 0 (dec (count hist)))})
                           pair (s/gen :unq/gift-pair)]
                          [hist year pair]))
        :ret :unq/gift-history)


(def int-or-nil (gen/one-of [[gen/int gen/int gen/int] [(gen/return nil) (gen/return nil) (gen/return nil)]))

(def testing (gen/one-of [(gen/let [hist (s/gen :unq/gift-history)
                                    year (gen/large-integer* {:min 0 :max (max 0 (dec (count hist)))})
                                    pair (s/gen :unq/gift-pair)]
                                   [hist year pair])
                          (gen/tuple (gen/return nil) gen/int (gen/map gen/keyword gen/keyword))]))

(def valid-or-nil (gen/tuple (gen/return nil) gen/int (gen/map gen/keyword gen/keyword)))


(gen/sample
  (gen/one-of [(gen/let [hist (s/gen :unq/gift-history)
                         year (gen/large-integer* {:min 0 :max (max 0 (dec (count hist)))})
                         pair (s/gen :unq/gift-pair)]
                        [hist year pair])
               (gen/tuple (gen/return nil) gen/int (gen/map gen/keyword gen/keyword))]) 3)


(set-gift-pair-in-gift-history [{:givee :P_*/R, :giver :r.*m!.HS?o/Wg*}
                                {:givee :!Pl.*_.w5.++4/Va?, :giver :K*/g}
                                {:givee :IN._-?/*1, :giver :Y+}
                                {:givee :CN!/r8K, :giver :s0/?f}
                                {:givee :F_*.B.W*4/U!*, :giver :I.?g_.!p!Y1.A+??+/?h}
                                {:givee :?.a?W25.++8i5/r-?, :giver :D+_0f.Pp/Fj1y4}
                                {:givee :?0m.oN_.-?k.PB!o/ET+*, :giver :Q}
                                {:givee :wIf+/+XU, :giver :h-3k!.THg/_0lV-}
                                {:givee :d.h6-z!.Ma6X.*?1!/lDCN, :giver :s2}
                                {:givee :k!-/e369, :giver :iA!N.F1-.__0/+}
                                {:givee :Fqn?4/Db__, :giver :-.uzZ!/i6}
                                {:givee :d.nCO/!, :giver :VG.+/o-dg}
                                {:givee :!1.F7*8*.s_.E/ZJ2P+, :giver :g/Z}
                                {:givee :gUyJ.p.l2se/n6, :giver :_k!*v.hS/!}
                                {:givee :bO*6!, :giver :NvJ_}
                                {:givee :_B/-Xw, :giver :pW.s8.X*35.S_8_/t?}
                                {:givee :c+752.Sf-W4.LDE.?*/vt, :giver :Ax_xp.lxR/BCw7S}
                                {:givee :cj.p_/Oci0, :giver :!*52.q47.!.M3vN/afi4}
                                {:givee :t?0T.v!_.*/qT, :giver :Q+*.lS?j9.i9Y?r.uQH/VU__w}]
                               3
                               {:givee :!778h, :giver :u?-/gbKF})


(defn set-gift-pair-in-gift-history [g-hist g-year g-pair]
  (if (nil? g-hist)
    [{:giver :none, :givee :none}]
    (assoc g-hist g-year g-pair)))
(s/fdef set-gift-pair-in-gift-history
        :args (s/with-gen
                (s/or :input-hist (s/and
                                    (s/cat :g-hist :unq/gift-history
                                           :g-year (s/and int? #(> % -1))
                                           :g-pair :unq/gift-pair)
                                    #(<= (:g-year %) (count (:g-hist %))))
                      :input-nil (s/cat :g-hist nil? :g-year any? :g-pair any?)))
        #(gen/one-of [(gen/let [hist (s/gen :unq/gift-history)
                                year (gen/large-integer* {:min 0 :max (max 0 (dec (count hist)))})
                                pair (s/gen :unq/gift-pair)]
                               [hist year pair])
                      (gen/tuple (gen/return nil) gen/int (gen/map gen/keyword gen/keyword))]))
:ret :unq/gift-history )
