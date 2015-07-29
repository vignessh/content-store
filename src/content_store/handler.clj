(ns content-store.handler
  (:require [content-store.db.mongo :as m]))

(defn init [{:keys [server port]} options]
  (m/mongo-connection server port))
