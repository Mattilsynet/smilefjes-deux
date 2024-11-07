(ns mattilsynet.design
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [superstring.core :as str]
            [umlaut.core :refer [förcat]]))

(def class-idx
  (->> (förcat [[k classes] (->> (io/resource "mtds/styles.json")
                                 slurp
                                 json/parse-string)]
         [[k classes]
          [(keyword k) (str/split classes #" ")]])
       (into {})))

(defmacro classes [& classes]
  (cons 'list
        (förcat [class classes]
          (get class-idx class [class]))))

(defn load-svg
  "Loads an SVG from the design system"
  [path]
  (slurp (io/resource (str "mtds/" path ".svg"))))

(def ^:export logo (load-svg "logo/logo"))
(def ^:export logo-engelsk (load-svg "logo/logo-engelsk"))
(def ^:export logo-samisk (load-svg "logo/logo-samisk"))
(def ^:export logo-symbol (load-svg "logo/symbol"))
