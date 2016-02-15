(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'cljs-rete4f.core
   :output-to "out/cljs_rete4f.js"
   :output-dir "out"})
