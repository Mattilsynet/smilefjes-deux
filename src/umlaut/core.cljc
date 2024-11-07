(ns umlaut.core
  "Non-lazy functions and macros you can't believe is not in clojure.core.
  Functions are tastefully named with umlauts as to be safely referrable without
  needing to exclude clojure.core functions.")

(defmacro ^:export för
  "Not to be confused with clojure.core/for, för is just a different way to
  express an eager mapping. It only supports a single binding form."
  [[binding coll] & body]
  `(mapv (fn [~binding]
           ~@body) ~coll))

(defmacro ^:export förcat [[binding coll] & body]
  `(->> ~coll
        (reduce
         (fn [coll# ~binding]
           (reduce (fn [xs# x#]
                     (conj! xs# x#)) coll# (do ~@body)))
         (transient []))
        persistent!))
