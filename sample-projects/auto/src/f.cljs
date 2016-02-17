(ns f
  (:require [goog.dom :as dom]
            [cljs-rete4f.core :as rete]))

(def eres (dom/getElement "result"))
(def midl (volatile! nil))

(defn println-browser [ln]
  (let [ores (.-innerHTML eres)
        nres (str ores "<br>" ln)]
    (set! (.-innerHTML eres) nres)))

(defn ask [q ops id]
  (let [ores (.-innerHTML eres)
        sel (str "<select onchange='javascript:f.ans(this.value)'>"
                 "<option value='select'>select</option>"
                 (apply str
                        (for [op ops]
                          (str "<option value='" op "'>" op "</option>")))
                 "</select>")
        qres (str ores "<br>" q)
        sres (str qres "<br>" sel)]
    (vreset! midl qres)
    (set! (.-innerHTML eres) sres)
    (rete/assert-frame ['asked 'question id])))

(defn ask-yorno [q id]
  (ask q ["yes" "no"] id))

(defn ans [val]
  (set! (.-innerHTML eres) @midl)
  (println-browser val)
  (rete/assert-frame ['received 'answer val])
  (rete/fire))
