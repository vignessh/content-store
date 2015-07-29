(ns content-store.entitlement-service)

(defn ^Boolean is-permitted 
  [^String tenant-id
   ^String user-id
   ^String resource
   ^String permission]
  (Boolean/FALSE))
