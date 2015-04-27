(ns rhodessquid.db
  (:require [yesql.core :refer [defqueries]])
  (:require [clojure.java.jdbc :as jdbc])
  (import 'org.postgresql.util.PGobject))


(defqueries "rhodessquid/queries.sql")

(defn to-hstore [m]
  (doto (PGobject.)
    (.setType "hstore")
    (.setValue
     (apply str
            (interpose ", "
                       (for [[k v] m]
                         (format "\"%s\"=>\"%s\"" (name k) v)))))))

(extend-type clojure.lang.IPersistentMap
  jdbc/ISQLParameter
  (set-parameter [val stmt ix]
    (.setObject stmt ix (to-hstore val))))
