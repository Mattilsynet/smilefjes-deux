(ns smilefjes.dev
  (:require [clojure.tools.namespace.repl :as repl]
            [datomic-type-extensions.api :as d]
            [powerpack.dev :as dev]
            [smilefjes.core :as smilefjes]
            [snitch.core]))

(defmethod dev/configure! :default []
  (repl/set-refresh-dirs "src" "dev" "test")
  (smilefjes/create-app :dev))

(defn start []
  (set! *print-namespace-maps* false)
  (dev/start))

(defn ->map [x]
  (cond
    (:db/id x) (update-vals (into {:db/id (:db/id x)} x) ->map)
    (map? x) (update-vals x ->map)
    (coll? x) (map ->map x)
    :else x))

(comment
  (def db (d/db (:datomic/conn (dev/get-app))))

  (d/q '[:find ?e .
         :where
         [?e :tilsynsbesøk/id]]
       db)

  (->map (d/entity db [:tilsynsbesøk/id "Z1601051557592250240VTAHG_TilsynAvtale"]))

  (map :spisested/navn (:spisested/_poststed (d/entity db [:poststed/postnummer "1610"])))
  ;; => ("Seiersten kafe Fasvo Bakeriutsalg" "Plankebyen Kafé")

  (:kommune/navn (:poststed/kommune (d/entity db [:poststed/postnummer "1610"])))
  ;; => "FREDRIKSTAD"

  (->> (d/entity db [:poststed/postnummer "1610"])
       :poststed/kommune
       :poststed/_kommune
       (mapcat :spisested/_poststed)
       (map :spisested/navn))

  )

(comment ;; s-:
  (start)
  (dev/reset))
