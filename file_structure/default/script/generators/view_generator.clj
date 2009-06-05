(ns generators.view-generator
  (:import [java.io File FileWriter])
  (:use [conjure.view.view :as view]))

(defn
#^{:doc "Prints out how to use the generate migration command."}
  view-usage []
  (println "You must supply a controller and action name (Like hello-world).")
  (println "Usage: ./run.sh script/generate.clj view <controller> <action>"))
  
(defn
#^{:doc "Generates the migration content and saves it into the given migration file."}
  generate-file-content
    ([view-file controller] (generate-file-content view-file controller nil))
    ([view-file controller content]
      (let [view-namespace (view/view-namespace controller view-file)
            content (str "(ns " view-namespace ")
;; Enter your view code here. The form in this file should return a string which is the content of your html file.")
          view-file-writer (new FileWriter view-file)]
      (. view-file-writer write content 0 (. content length))
      (. view-file-writer flush)
      (. view-file-writer close))))

(defn
#^{:doc "Creates the migration file from the given migration-name."}
  generate-view-file
    ([controller action] (generate-view-file controller action nil))
    ([controller action content]
      (if (and controller action)
        (let [view-directory (view/find-views-directory)]
          (if view-directory
            (let [controller-directory (view/find-or-create-controller-directory view-directory controller)
                  view-file (view/create-view-file controller-directory action)]
                (if view-file
                  (generate-file-content view-file controller content)))
            (do
              (println "Could not find views directory.")
              (println view-directory))))
        (view-usage))))

(defn 
#^{:doc "Generates a migration file for the migration name given in params."}
  generate-view [params]
  (generate-view-file (first params) (second params)))