(ns content-store.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [compojure.api.middleware :as middleware]
            [schema.core :as s]))

(defn service-routes [{:keys [db] :as system}]
  (api
   (ring.swagger.ui/swagger-ui "swagger-ui")
   (swagger-docs {:info {:title "Content Store API"}})
   (context* "/:tenant-id/document" [tenant-id]
             :tags ["document"]
             :path-params [tenant-id :- String]
             :header-params [username :- String, shortname :- String]
             (GET* "/:id" [id :as request]
                   :path-params [id :- String]
                   :summary "Returns the document identified by the given identifier"
                   (ok {:createdBy username :creatorShortName shortname :tenant-id tenant-id}))
             (POST* "/" [:as request]
                    :summary "Attach given metadata alongwith uploaded document"
                    (let [content (request :body)
                          meta (merge (content :metadata) {:createdBy username :creatorShortName shortname})
                          document (assoc content :metadata meta)]
                      (ok document))))))

(defapi api-routes
  (ring.swagger.ui/swagger-ui "swagger-ui")
  (swagger-docs {:info {:title "Content Store API"}})
  (context* "/:tenant-id/document" [tenant-id]
            :tags ["document"]
            :path-params [tenant-id :- String]
            :header-params [username :- String, shortname :- String]
            :components [db]
            (GET* "/:id" [id :as request]
                  :path-params [id :- String]
                  :summary "Returns the document identified by the given identifier"
                  (ok {:createdBy username :creatorShortName shortname :tenant-id tenant-id}))
            (POST* "/" [:as request]
                   :summary "Attach given metadata alongwith uploaded document"
                   (let [content (request :body)
                         meta (merge (content :metadata) {:createdBy username :creatorShortName shortname})
                         document (assoc content :metadata meta)]
                     (ok document)))))
