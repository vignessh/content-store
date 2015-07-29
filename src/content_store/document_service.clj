(ns content-store.document-service
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            [monger.joda-time :as mj])
  (:import [com.mongodb DB MongoClient MongoOptions ServerAddress])
  (:import org.bson.types.ObjectId)
  (:import org.joda.time.DateTime))

(def document
  [:metadata :uploadedOn :modifiedOn :documentType :uri :tenantId :title :size :userTags :systemTags :createdBy :description :isPublic :shares :checksum :fileName :status])

;(def collection "b2b.contentstore.document")

(defn with-mongo-connection "A convenient way to use a connection for doing any operation on a given collection."
	[function & args]
  (let [^ServerAddress server-address (mg/server-address "localhost" 27017)
        ^MongoOptions options (mg/mongo-options {:connections-per-host 30})
        ^String collection "b2b.contentstore.document"]
    (with-open [^MongoClient connection (mg/connect server-address options)]
      (let [^DB b2b (mg/get-db connection "b2b")]
    	(apply function b2b collection args)))))

(def ^ServerAddress server-address (mg/server-address "localhost" 27017))
(def ^MongoOptions mongo-options (mg/mongo-options {:connections-per-host 30}))
(def ^String collection "b2b.contentstore.document")
(def ^MongoClient client (mg/connect server-address mongo-options))

(defn with-mongo-connection2 [function & args]
  (let [^DB b2b (mg/get-db client "b2b")]
    (apply function b2b collection args)))

(defn find-by-id "Find by tenant id and document id"
  [^String tenant-id ^String id]
  (with-mongo-connection2 mc/find-one-as-map {:_id (ObjectId. id) :tenantId tenant-id}))

(defn update "Update the given document's key with the given value"
  [^String tenant-id ^String id ^String key ^Object value]
  (with-mongo-connection2 mc/update {:tenantId tenant-id :_id (ObjectId. id)} {key value} {:upsert true}))

(defn store [^String tenant-id document]
	(let [{:keys [metadata 
                 ^DateTime uploadedOn 
                 ^DateTime modifiedOn 
                 ^String documentType 
                 ^String uri 
                 ^String tenantId 
                 ^String title 
                 ^Long size 
                 userTags 
                 systemTags 
                 ^String createdBy 
                 ^String description 
                 ^Boolean isPublic 
                 shares 
                 ^String checksum 
                 ^String fileName 
                 ^String status]
           :or {uploadedOn (org.joda.time.DateTime.)
                isPublic Boolean/FALSE 
                userTags() 
                systemTags()}} document]
           (prn tenant-id uri description isPublic uploadedOn)))
