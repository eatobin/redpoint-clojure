;(s/or :input-hist (s/cat :g-hist :unq/gift-history
;                         :g-year (s/and int? #(> % -1))
;                         :g-pair :unq/gift-pair)
;      :input-nil (s/cat :g-hist nil?
;                        :g-year (s/and int? #(> % -1))
;                        :g-pair :unq/gift-pair))
;
;
;(s/or :input-hist (s/cat :g-hist :unq/gift-history
;                         :plr :unq/player)
;      :input-nil (s/cat :g-hist nil?
;                        :plr :unq/player))
