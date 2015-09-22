(defproject rhodessquid "0.1.0-SNAPSHOT"
  :description "Resource based i18n app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.8.11"]
            [ragtime/ragtime.lein "0.3.8"]
            [cider/cider-nrepl "0.9.0-SNAPSHOT"]
            [refactor-nrepl "1.1.0"]
            [lein-environ "1.0.0"]
            [lein-asset-minifier "0.2.2"]]

  :ring {:handler rhodessquid.core/handler
         :uberwar-name "rhodessquid.war"}

  :min-lein-version "2.5.0"
  :uberjar-name "rhodessquid.jar"

  :main rhodessquid.core

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]


  :source-paths ["src/clj" "src/cljc"]

  :minify-assets
  {:assets
   {"resources/public/css/site.min.css" "resources/public/css/site.css"}}


  :cljsbuild {:builds {:app {:source-paths ["src/cljs" "src/cljc"]
                             :compiler {:output-to "resources/public/js/app.js"
                                        :output-dir "resources/public/js/out"
                                        :asset-path "js/out"
                                        :optimizations :none
                                        :pretty-print true}}}}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [liberator "0.12.2"]
                 [org.clojure/data.json "0.2.6"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [yesql "0.4.2"]
                 [org.clojure/tools.logging "0.3.1"]
                 [log4j/log4j "1.2.16"]
                 [ragtime "0.3.9"]
                 [org.clojure/tools.nrepl "0.2.8"]
                 [postgresql/postgresql "8.4-702.jdbc4"]
                 [expectations "2.1.1"]
                 [org.clojure/clojurescript "1.7.107" :scope "provided"]
                 [reagent "0.5.1-rc"]
                 [reagent-forms "0.5.6"]
                 [reagent-utils "0.1.5"]
                 [secretary "1.2.3"]
                 [cljsjs/react "0.13.3-1"]
                 [prone "0.8.2"]
                 [environ "1.0.0"]]

  :ragtime {:migrations ragtime.sql.files/migrations
            :database "jdbc:postgresql://localhost:5432/rhodessquid"}

  :profiles {:dev {:repl-options {:init-ns rhodessquid.repl}
                   :dependencies [[ring/ring-mock "0.2.0"]
                                  [ring/ring-devel "1.4.0"]
                                  [lein-figwheel "0.3.7"]
                                  [org.clojure/tools.nrepl "0.2.10"]
                                  [pjstadig/humane-test-output "0.7.0"]]

                   :source-paths ["env/dev/clj"]

                   :plugins [[lein-figwheel "0.3.7"]
                             [lein-cljsbuild "1.0.6"]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :nrepl-port 7002
                              :css-dirs ["resources/public.css"]
                              :ring-handler rhodessquid.core/app}

                   :env {:dev true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                                              :compiler {:main "rhodessquid.dev"
                                                         :source-map true}}
                                        }
                               }}
             :uberjar {:hooks [leiningen.cljsbuild minify-assets.plugin/hooks]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :cljsbuild {:jar true
                                   :builds {:app
                                            {:source-paths ["env/prod/cljs"]
                                             :compiler
                                             {:optimizations :advanced
                                              :pretty-print false }}}}}})
