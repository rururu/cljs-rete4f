(ns f
  (:require [goog.dom :as dom]))

(def eres (dom/getElement "result"))

(defn println-browser [ln]
  (let [ores (.-innerHTML eres)
        nres (str ores "<br>" ln)]
    (set! (.-innerHTML eres) nres)))


