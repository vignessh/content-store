(defproject content-store "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 ;[com.novemberain/welle "2.0.0-beta1"]
                 ;;[io.pedestal/pedestal.service "0.2.2"]
                 ;;[io.pedestal/pedestal.service-tools "0.2.2"]

                 ;;[ns-tracker "0.2.1"]

                 ;; Remove this line and uncomment the next line to
                 ;; use Tomcat instead of Jetty:
                 ;;[io.pedestal/pedestal.jetty "0.2.2"]
                 [compojure "1.3.4"]
                 [ring "1.4.0-RC1"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-defaults "0.1.5"]
                 [com.novemberain/monger "3.0.0-rc1"]
                 [aleph "0.4.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [cheshire "5.5.0"]
                 [prismatic/schema "0.4.3"]
                 [metosin/compojure-api "0.22.0"]
                 [metosin/ring-swagger-ui "2.1.1-M2"]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.2"]
                 [com.stuartsierra/component "0.2.3"]
                 [org.danielsz/system "0.1.1"]
                 [environ "1.0.0"]]
                 ;; [io.pedestal/pedestal.tomcat "0.2.2"]
  ;;:profiles {:dev {:source-paths ["dev"]}}
  ;;:aliases {"run-dev" ["trampoline" "run" "-m" "dev"]}
  :min-lein-version "2.3.3"
  :jvm-opts ^:replace ["-server" "-Xmx2g"]  
  :main content-store.core
  :plugins [[lein-environ "1.0.0"]]
  ;:ring {:handler content-store.document-handler/app :port 8080}
  ;:uberjar-name "content-store.jar"
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}
  :aot :all)
