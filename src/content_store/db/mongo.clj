(ns content-store.db.mongo
  (:require [monger.core :as mg]
            [com.stuartsierra.component :as component])
  (:import [com.mongodb DB MongoClient MongoOptions ServerAddress]))

(defrecord Mongo [host port database options connection]
  component/Lifecycle

  (start [component]
    (println ";; Starting Mongo connection")
    (if connection
      component
      (let [^ServerAddress address (mg/server-address server port)
            ^MongoClient client (mg/connect address (mg/mongo-options options))
            ^DB db (mg/get-db client database)]
        (assoc component :connection db))))

  (stop [component]
    (println ";; Stopping Mongo connection")
    (if not connection
        component
        (do (.close connection)
            (assoc component :connection nil)))))

(defn new-mongo-connection 
  [{:keys [^String server ^Integer port ^String database options]
    :or {server "localhost" 
         port 27017 
         options {:connections-per-host 30} 
         database "b2b"}}]
  (map->Mongo {:host server 
               :port port 
               :database database 
               :options mongo-options}))
