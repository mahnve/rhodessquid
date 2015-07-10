(defproject rhodessquid "0.1.0-SNAPSHOT"
  :description "Resource based i18n app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.8.11"]
            [ragtime/ragtime.lein "0.3.8"]
            [cider/cider-nrepl "0.9.0-SNAPSHOT"]
            [refactor-nrepl "1.1.0"]]
  :ring {:handler rhodessquid.core/handler}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [liberator "0.12.2"]
                 [compojure "1.3.2"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-core "1.3.2"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [yesql "0.4.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [log4j/log4j "1.2.16"]
                 [ragtime "0.3.8"]
                 [org.clojure/tools.nrepl "0.2.8"]
                 [postgresql/postgresql "8.4-702.jdbc4"]
                 [expectations "2.0.9"]]

  :ragtime {:migrations ragtime.sql.files/migrations
            :database "jdbc:postgresql://localhost:5432/rhodessquid"})
