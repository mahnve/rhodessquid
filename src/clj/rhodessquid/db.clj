(ns rhodessquid.db
  (:require [yesql.core :refer [defqueries]])
  (:require [clojure.java.jdbc :as jdbc])
  (import org.postgresql.util.PGobject))


(defqueries "rhodessquid/queries.sql")
