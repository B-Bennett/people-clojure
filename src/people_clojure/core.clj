(ns people-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.pprint :as pprint])
  (:gen-class))

(defn -main [& args]
 (let [people (slurp "people.csv")
       people (str/split-lines people)
       people (map (fn [line]
                     (str/split line #","))
                   people)
       header (first people)
       people (rest people)
       people (map (fn [line]
                     (interleave header line))
                   people)
       people (map (fn [line]
                     (apply hash-map line))
                   people)
       people (walk/keywordize-keys people)
       people (filter (fn [line]
                        (= "Brazil" (:country line)))
                      people)]
   (spit "filtered_people.edn"
         (with-out-str (pprint/pprint people)))))

