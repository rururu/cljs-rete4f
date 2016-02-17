(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'auto.core
   :output-to "out/auto.js"
   :output-dir "out"})
