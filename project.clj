(defproject clojure-redpoint "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clojure-csv/clojure-csv "2.0.2"]
                 [orchestra "2017.11.12-1"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}
  :main clojure-redpoint.main)
