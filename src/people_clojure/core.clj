(ns people-clojure.core
(:require [clojure.string :as str]
          [clojure.walk :as walk]
          [clojure.pprint :as pprint]
          [ring.adapter.jetty :as j]
          [hiccup.core :as h])
(:gen-class))
(defn read-people []
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
  #_(spit "filtered_people.edn"
        (with-out-str (pprint/pprint people)))
  people))

(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (h/html [:html
                     [:body
                      [:a {:href "http://www.theironyard.com"}
                       "The Iron Yard"]
                      [:br]
                      (let [people (read-people)]
                        (map (fn [line]
                               [:p
                                (str (:first_name line)
                                     "  "
                                     (:last_name line))])
                             people))]])})

(defn -main [& args]
 (j/run-jetty #'handler {:port 3000 :join? false}))

