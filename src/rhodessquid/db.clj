(ns rhodessquid.db
  (:require [yesql.core :refer [defqueries]]))

(defqueries "rhodessquid/queries.sql")
