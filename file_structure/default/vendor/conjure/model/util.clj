(ns conjure.model.util
  (:require [clojure.contrib.seq-utils :as seq-utils]
            [clojure.contrib.str-utils :as contrib-str-utils]
            [conjure.util.loading-utils :as loading-utils]
            [conjure.util.file-utils :as file-utils]
            [conjure.util.string-utils :as string-utils]
            [clj-record.util :as clj-record-util]))

(defn
#^{ :doc "Returns the model name for the given model file." }
  model-from-file [model-file]
  (if model-file
    (loading-utils/clj-file-to-symbol-string (. model-file getName))))
  
(defn
#^{ :doc "Returns the model namespace for the given model." }
  model-namespace [model]
  (if model (str "models." model)))
  
(defn
#^{ :doc "Loads the namespace for the given model." }
  load-model [model]
  (require (symbol (model-namespace model))))

(defn
#^{ :doc "Runs the given function in the given model with the given parameters." }
  run-model-fn [model function & params]
  (load-model model)
  (apply (ns-resolve (find-ns (symbol (model-namespace model))) (symbol function)) params))

(defn 
#^{ :doc "Finds the models directory." }
  find-models-directory []
  (seq-utils/find-first (fn [directory] (. (. directory getPath) endsWith "models"))
    (. (loading-utils/get-classpath-dir-ending-with "app") listFiles)))

(defn
#^{ :doc "Returns all of the model files in the models directory." }
  model-files []
  (filter loading-utils/clj-file? (file-seq (find-models-directory))))

(defn
#^{ :doc "Returns the model namespace for the given model file." }
  model-file-namespace 
  [model-file]
  (loading-utils/file-namespace (.getParentFile (find-models-directory)) model-file))

(defn
#^{ :doc "Returns a sequence of all model namespaces." }
  all-model-namespaces []
  (map #(symbol (model-file-namespace %)) (model-files)))

(defn
#^{ :doc "Returns the model file name for the given model name." }
  model-file-name-string [model-name]
  (if model-name (str (loading-utils/dashes-to-underscores model-name) ".clj")))
  
(defn
#^{ :doc "Returns the name of the migration associated with the given model." }
  migration-for-model [model]
  (if model (str "create-" (clj-record-util/pluralize model))))
  
(defn
#^{ :doc "Returns the table name for the given model." }
  model-to-table-name [model]
  (if model (clj-record-util/pluralize (loading-utils/dashes-to-underscores model))))

(defn
#^{ :doc "Finds a model file with the given model name." }
  find-model-file
  ([model-name] (find-model-file (find-models-directory) model-name)) 
  ([models-directory model-name]
    (if (and models-directory model-name)
      (file-utils/find-file models-directory (model-file-name-string model-name)))))
      
(defn
#^{ :doc "Returns the model name for the given belongs to column." }
  to-model-name [belongs-to-column]
  (string-utils/strip-ending belongs-to-column "-id"))