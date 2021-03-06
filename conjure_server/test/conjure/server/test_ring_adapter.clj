(ns conjure.server.test-ring-adapter
  (:use clojure.test
        conjure.server.ring-adapter
        test-helper))

(def service-name "test")
(def action-name "show")
(def id "1")
(def uri (str "/" service-name "/" action-name "/" id))

(use-fixtures :once init-server)

(deftest test-call-server
  (is
    (call-server 
      { :uri uri
        :query-string "foo=bar&baz=biz" })))

(defn noop-app [request]
  nil)

(deftest test-wrap-resource-dir
  (is ((wrap-resource-dir noop-app "public") { :request-method :get, :uri "/stylesheets/main.css" }))
  (is (nil? ((wrap-resource-dir noop-app "public") { :request-method :get, :uri "/" }))))

(deftest test-conjure
  (is (conjure 
        { :uri uri
          :query-string "foo=bar&baz=biz" }))
  (is (conjure 
        { :uri "/" 
          :query-string "" })))