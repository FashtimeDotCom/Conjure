(ns views.layouts.templates.links-sidebar
  (:use conjure.view.base)
  (:require [conjure.util.conjure-utils :as conjure-utils]
            [conjure.util.request :as request]
            [views.layouts.templates.links :as links]))

(defn
#^{ :doc "Creates a list link for use in the link sidebar." }
  list-link [layout-info]
    { :text "List", :url-for (conjure-utils/merge-url-for-params layout-info { :action "list-records" }) })

(defn
#^{ :doc "Creates an add link for use in the link sidebar." } 
  add-link [layout-info]
    { :text "Add", 
      :url-for (conjure-utils/merge-url-for-params layout-info { :action "add" }),
      :html-options { :id "add-action-link" } })

(def-view []
  (let [layout-info (request/layout-info)
        links (:links layout-info)]
      (links/render-body "Actions"
        (if links
          links
          [ (list-link layout-info)
            (add-link layout-info)]))))