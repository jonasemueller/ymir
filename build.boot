;; Copyright © 2016, JUXT LTD.

;; A complete development environment for websites in Clojure and
;; ClojureScript.

;; Most users will use 'boot dev' from the command-line or via an IDE
;; (e.g. CIDER).

;; See README.md for more details.

(def project "ymir")
(def version (or (System/getenv "VERSION")
                 "development"))

(set-env!
 ;; It's okay for "test" to be used in source-paths as they don't go into
 ;; resulting jar unless AOT'd.
 :source-paths #{"sass" "src" "test"}
 :resource-paths #{"resources"}
 :dependencies
 '[[adzerk/boot-cljs "2.1.4" :scope "test"]
   [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
   [adzerk/boot-reload "0.5.2" :scope "test"]
   [weasel "0.7.0" :scope "test"] ;; Websocket Server
   [deraen/boot-sass "0.3.1" :scope "test"]
   [reloaded.repl "0.2.4" :scope "test"]
   [day8.re-frame/trace "0.1.14" :scope "test"]
   [binaryage/devtools "0.9.9" :scope "test"]
   [org.clojure/test.check "0.9.0" :scope "test"]
   [pjstadig/humane-test-output "0.8.3"]
   [boot-deps "0.1.9"]

   [org.clojure/clojure "1.9.0"]
   [org.clojure/clojurescript "1.9.946"]

   [org.clojure/tools.nrepl "0.2.13"]

   ;; Needed for start-repl in cljs repl
   [com.cemerick/piggieback "0.2.2" :scope "test"]

   ;; Needed for Leiningen support
   [onetom/boot-lein-generate "0.1.3" :scope "test"]

   ;; Code quality
   [tolitius/boot-check "0.1.6"]

   ;; Server deps
   [aero "1.1.2"]
   [bidi "2.1.3"]
   [kibu/pushy "0.3.8"]
   [com.stuartsierra/component "0.3.2"]
   [hiccup "1.0.5"]
   [org.clojure/tools.namespace "0.3.0-alpha4"]
   [prismatic/schema "1.1.7"]
   [selmer "1.11.5"]
   [yada "1.2.10" :exclusions [ring-swagger]]
   ;; https://github.com/juxt/yada/pull/181
   [org.clojure/core.async "0.4.474"]
   [aleph "0.4.4"]
   [metosin/ring-swagger "0.25.0"]
   [org.clojure/tools.cli "0.3.5"]

   ;; App deps
   [reagent "0.7.0"]
   [org.jboss/jboss-vfs "3.2.12.Final"]
   [org.clojure/tools.cli "0.3.5"]
   [org.clojure/tools.logging "0.4.0"]
   [org.webjars/modernizr "2.8.3-1"]
   [cljs-ajax "0.7.3"]
   [reagent-utils "0.2.1"]
   [re-frame "0.10.2"]

   ;; Logging
   [org.clojure/tools.logging "0.4.0"]
   [org.slf4j/jcl-over-slf4j "1.7.25"]
   [org.slf4j/jul-to-slf4j "1.7.25"]
   [org.slf4j/log4j-over-slf4j "1.7.25"]
   [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]])


(require '[boot.lein])
(boot.lein/generate)

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[adzerk.boot-reload :refer [reload]]
         '[deraen.boot-sass :refer [sass]]
         '[com.stuartsierra.component :as component]
         '[tolitius.boot-check :as check]
         'clojure.tools.namespace.repl)

(def repl-port 5600)

(task-options!
 repl {:client true
       :port repl-port}
 pom {:project (symbol project)
      :version version
      :description "A complete Clojure project you can leap from"
      :license {"The MIT License (MIT)" "http://opensource.org/licenses/mit-license.php"}}
 aot {:namespace #{'example.ymir.alpha.main}}
 jar {:main 'example.ymir.alpha.main
      :file (str project "-app.jar")})

;; Currently CLJS dependencies cannot be required at the REPL.
;; See https://github.com/adzerk-oss/boot-cljs-repl/issues/49

;; See https://github.com/boot-clj/boot/wiki/Snippets#access-a-version-string-at-runtime-from-a-jar
(deftask add-version-txt []
  (with-pre-wrap fs
    (let [t (tmp-dir!)]
      (spit (clojure.java.io/file t "version.txt") version)
      (-> fs (add-resource t) commit!))))

(deftask dev-system
  "Develop the server backend. The system is automatically started in
  the dev profile."
  []
  (let [run? (atom false)]
    (with-pass-thru _
      (when-not @run?
        (reset! run? true)
        (require 'reloaded.repl)
        (let [go (resolve 'reloaded.repl/go)]
          (try
            (require 'user)
            (go)
            (catch Exception e
              (boot.util/fail "Exception while starting the system\n")
              (boot.util/print-ex (.getCause e)))))))))

(deftask dev
  "This is the main development entry point."
  []
  (set-env! :source-paths #(conj % "dev"))

  ;; Needed by tools.namespace to know where the source files are
  (apply clojure.tools.namespace.repl/set-refresh-dirs (get-env :directories))

  (comp
   (watch)
   (speak)
   (sass :output-style :expanded)
   (reload :on-jsload 'example.ymir.alpha.core/init!)
   (dev-system)
   ; this is also the server repl!
   (cljs-repl :nrepl-opts {:client false
                           :port repl-port
                           :init-ns 'user})
   (cljs :optimizations :none
         :compiler-options {:closure-defines {"re_frame.trace.trace_enabled_QMARK_" true}
                            :preloads ['day8.re-frame.trace.preload]})
   (target)))

(deftask static
  "This is used for creating optimized static resources under static"
  []
  (comp
   (sass :output-style :compressed)
   (cljs :optimizations :advanced
         :compiler-options {:infer-externs true
                            :pretty-print false
                            :closure-warnings
                            {:externs-validation :off :non-standard-jsdoc :off}})))

(deftask build
  []
  (comp
   (static)
   (target :dir #{"static"})))

(deftask run-system
  [p profile VAL kw "Profile to start system with"]
  (require 'example.ymir.alpha.system)
  (let [new-system (resolve 'example.ymir.alpha.system/new-system)]
    (with-pre-wrap fileset
      (let [system (new-system profile)]
        (component/start system)
        (intern 'boot.user 'system system)
        (assoc fileset :system system)))))

(deftask run
  [p profile VAL kw "Profile"]
  (comp
   (repl :server true
         :port (case profile :prod 5601 :beta 5602 5600))
   (run-system :profile (or profile :prod))
   (wait)))

(deftask uberjar
  "Build an uberjar"
  []
  (println "Building uberjar")
  (comp
   (static)
   (aot)
   (pom)
   (add-version-txt)
   (uber)
   (jar)
   (target)))

(deftask show-version "Show version" [] (println version))

(deftask check-sources []
  (set-env! :source-paths #{"src" "test"})
  (comp
   (check/with-yagni)
   (check/with-eastwood)
   (check/with-kibit)
   (check/with-bikeshed)))

