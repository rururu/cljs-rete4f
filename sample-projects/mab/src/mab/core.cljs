(ns mab.core
  (:require templates rules f facts [cljs-rete4f.core :as rete]))

(rete/run-with "run" templates/TEMPLATES rules/RULES facts/FACTS)
