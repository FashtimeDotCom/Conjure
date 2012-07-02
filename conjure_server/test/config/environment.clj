(ns config.environment)

(def properties
  (atom 
    { :default-environment "test"

      :source-dir "test"

      :call-controller-fn (fn []
                            (require 'conjure.flow.util)
                            (ns-resolve 'conjure.flow.util 'call-controller)) }))
