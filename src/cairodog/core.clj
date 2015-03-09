(ns cairodog.core)
(:use [liberator.core :only [defresource]])

(defresource helloworld
  :available-media-types ["text/plain"])
  (defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
