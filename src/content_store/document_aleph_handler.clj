(ns content-store.document-aleph-handler
  (:require 
   [compojure.core :as compojure :refer [GET PUT DELETE POST]]
   [aleph.http :as http]
   [compojure.route :as route]
   [clojure.tools.cli :as cli]
   [ring.middleware.json :as rmj]
   [ring.util.response :as rr]
   [content-store.document-new-service :as service]
   [ring.middleware.defaults :as rmd]
   [ring.middleware.params :as params]
   [monger.core :as mg]
   [schema.core :as s]
   [compojure.api.sweet :refer :all]
   [ring.util.http-response :refer :all])
  (:import [com.mongodb DB MongoClient MongoOptions ServerAddress]))

(defn- mongo-connection 
  [^String server-address 
   ^Integer port & 
   {:keys [mongo-options ^String database] 
    :or {mongo-options {:connections-per-host 30} database "b2b"}}]
  (let [^ServerAddress address (mg/server-address server-address port)
        ^MongoClient client (mg/connect address (mg/mongo-options mongo-options))]
    (mg/get-db client database)))

(defn get-document [^String tenant-id ^String id]
 ; (rr/response (service/find-by-id tenant-id id))
  )

(defn upload-document [^String tenant-id content]
  ;(rr/response (service/store tenant-id content))
  )

(defn handler "The routing table for our service" [{:keys [db]}]
  (compojure/routes
   (compojure/context "/:tenant-id/document" [tenant-id :as {{username "username" shortName "shortName"} :headers}]
       (GET "/:id" [id] 
            (let [document (service/find-by-id db tenant-id id username)] 
              (if-not (empty? document) 
                (rr/response document)
                {:status 404
                 :body (str "Requested document with #'" id "' for tenant '" tenant-id "' is not found.")
                 :headers {"content-type" "text/plain"}})))
       (POST "/_uploadUrl" [:as request]
            (let [content (request :body)
                  meta (merge (content :metadata) {:createdBy username :creatorShortName shortName})
                  document (assoc content :metadata meta)]
              (rr/response (service/store db tenant-id document))))
     (route/not-found "No such endpoint"))))

;; Bug fix in ring-json, need to send a pull request
(defn wrap-correct-content-type [handler]
  (fn [request]
    (handler (assoc request :content-type (get-in request [:headers "content-type"])))))

(defn app "Add more middleware to make our endpoint JSON aware"
  [handler]
  (->
   (rmd/wrap-defaults handler rmd/api-defaults)
   (rmj/wrap-json-body {:keywords? true})
   (wrap-correct-content-type)
   ;(middleware/wrap-json-params)
   ;(middleware/wrap-content-type)
   (rmj/wrap-json-response {:pretty true :escape-non-ascii true})))

(def cli-options
  [["-p" "--port PORT" "Port number" 
    :default 8080 
    :parse-fn #(Integer/parseInt %) 
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options]} (cli/parse-opts args cli-options)
        ^DB db (mongo-connection "localhost" 27017)]
       (http/start-server (app (handler {:db db})) options)))


