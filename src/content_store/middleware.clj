(ns content-store.middleware
  (:require [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))

(defn- wrap-correct-content-type [handler]
  (fn [request]
    (handler (assoc request :content-type (get-in request [:headers "content-type"])))))

(defn handler [handler]
  (->
   (wrap-defaults handler api-defaults)
   (wrap-json-body {:keywords? true})
   (wrap-correct-content-type)
   (wrap-json-response {:pretty true :escape-non-ascii true})))
