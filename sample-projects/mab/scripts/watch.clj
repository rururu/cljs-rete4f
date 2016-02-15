(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'mab.core
   :output-to "out/mab.js"
   :output-dir "out"})
