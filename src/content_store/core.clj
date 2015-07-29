(ns content-store.core
  (:require [clojure.tools.cli :as cli]
            [aleph.http :as http]
            [content-store.db.mongo :as mongo]
            [content-store.middleware :as m]
            [content-store.routes.services :as s]
            [system.core :as system]
            [system.components.aleph :as aleph]
            [com.stuarsierra.component :as component]))

(defn dev-system []
  (component/system-map
   :mongo-db (mongo/new-mongo-connection {:server "localhost" :port 27017 :database "b2b"})
   :web (aleph/new-web-server {:port 8080 :handler m/service-routes})))

(def cli-options
  [["-p" "--port PORT" "Port number" 
    :default 8080 
    :parse-fn #(Integer/parseInt %) 
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options]} (cli/parse-opts args cli-options)]
    (http/start-server (m/handler (s/service-routes {:db (mm/mongo-connection {:server "localhost" :port 27017})})) options)))

