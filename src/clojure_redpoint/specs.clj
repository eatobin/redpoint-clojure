(ns clojure-redpoint.specs
  (:require [clojure-redpoint.roster-utility :as ru]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as gen]))

(gen/generate (s/gen int?))

(stest/check `ru/make-roster-seq)
