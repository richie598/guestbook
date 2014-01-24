(ns guestbook.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

;;(def db {:classname "org.sqlite.JDBC",
;;        :subprotocol "sqlite"
;;        :subname "db.sq3"})

(def db {:name "jdbc/EYMySQL"})

(defn create-guestbook-table []
  (sql/with-connection
   db
   (sql/create-table
    :guestbook
    [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
    [:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
    [:name "TEXT"]
    [:message "TEXT"])
   (sql/do-commands "CREATE INDEX timestamp_index ON guestbook (timestamp)")))

(defn table-exists [table-name]
  (sql/with-connection
   db
   (not 
    (nil? 
      (sql/with-query-results rs [(str "SHOW TABLES LIKE \"" table-name "\"")] (first rs))))))

(defn read-guests []
  (sql/with-connection
   db
   (sql/with-query-results res
     ["SELECT * FROM guestbook ORDER BY timestamp DESC"]
     (doall res))))

(defn save-message [name message]
  (sql/with-connection
   db
   (sql/insert-values
    :guestbook
    [:name :message :timestamp]
    [name message (new java.util.Date)])))
