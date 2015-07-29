(ns content-store.configure
  (:require [com.stuartsierra.component :as component]
            [content-store.db.mongo :as mongo]))

(def new-system [config]
  (component/system-map 
   :mongo (mongo/new-mongo-connection config)))
