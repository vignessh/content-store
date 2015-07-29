(ns content-store.document-new-service
 (:require [monger.collection :as mc]
           [monger.joda-time]
           [monger.json]
           [schema.core :as s]
           [content-store.entitlement-service :as es])
 (:import [com.mongodb DB])
 (:import org.bson.types.ObjectId)
 (:import org.joda.time.DateTime))

(def collection "b2b.contentstore.document")


(defn store [^DB db ^String tenant-id document]
 (let [{:keys [metadata
               ^DateTime uploadedOn
               ^DateTime modifiedOn
               ^String documentType
               ^String uri
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
               ^String status
               ^String creatorShortName]
        :or {uploadedOn (DateTime.)
             isPublic Boolean/FALSE
             userTags ()
             systemTags ()
             shares ()}} document]
   (mc/insert-and-return db collection 
                         {:uploadedOn uploadedOn
                          :modifiedOn modifiedOn
                          :documentType documentType
                          :uri uri
                          :title title
                          :tenantId tenant-id
                          :size size
                          :createdBy createdBy
                          :description description
                          :isPublic isPublic
                          :checksum checksum
                          :fileName fileName
                          :status "INPROGRESS"
                          :metadata metadata
                          :userTags userTags
                          :systemTags systemTags})))

(defn find-by-id [^DB db ^String tenant-id ^String document-id ^String user-id]
  (when-let [is-permitted (es/is-permitted tenant-id user-id (str "/files/" document-id) "READ")]
    (mc/find-one-as-map db collection {:_id (ObjectId. document-id) :tenantId tenant-id})))
