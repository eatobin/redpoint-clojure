args (s/or :input-hist (s/cat :g-hist :unq/gift-history :g-year int?) :input-nil (s/cat :g-hist nil? :g-year int?)) :ret (s/or :found :unq/gift-pair :not-found nil?))

