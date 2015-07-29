(ns content-store.document-handler
  (:use compojure.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.middleware.keyword-params :as keyword-params]
            [ring.util.response :as rr]
            [content-store.document-service :as service]))

(defn get-document[tenant-id id]
  ;(rr/response {:tenant tenant-id :id id})
  (service/find-by-id tenant-id id))

(defroutes app-routes
  (context "/:tenant-id/document" [tenant-id]
    ;(POST "/" ())
    ;(PUT "/:id" [id] (update-document id))
    (GET "/:id" [^String id] (get-document tenant-id id)))
    ;(GET "/:id" [id req] (str (req :headers))))
  (route/not-found "Not found"))

(defroutes app-routes2
  (GET "/:tenant-id/document/:id" [tenant-id id] (rr/response (service/find-by-id tenant-id id))))

(def app
  (->
    (handler/api app-routes2)
    (keyword-params/wrap-keyword-params)
    (middleware/wrap-json-body {:keywords? true})
    (middleware/wrap-json-response)))
