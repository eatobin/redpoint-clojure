(ns clojure-redpoint.specs
  (:require [clojure-redpoint.domain :as dom]
            [clojure-redpoint.roster-string-check :as rs]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as sgen]
            [clojure.test.check.generators :as gen]))

(sgen/generate (s/gen int?))

(gen/sample gen/int)
(gen/sample (gen/list gen/boolean))
(gen/sample (gen/tuple gen/nat gen/boolean gen/ratio))

(stest/check `rs/scrub)
(s/exercise-fn `rs/scrub)
(s/explain ::dom/scrubbed "jj")
