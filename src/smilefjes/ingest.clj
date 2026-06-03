(ns smilefjes.ingest
  (:require [clojure.java.io :as io]
            [datomic-type-extensions.api :as d]
            [java-time-literals.core]
            [powerpack.logger :as log]
            [smilefjes.import.postnummer :as postnummer]
            [smilefjes.import.tilsyn :as tilsyn]
            [smilefjes.import.vurderinger :as vurderinger]))

(defn on-started [powerpack]
  (let [start-ms (System/currentTimeMillis)]
    (log/info "Ingesting data/postnummer.csv ...")
    (postnummer/transact (:datomic/conn powerpack)
                         (io/file "data/postnummer.csv"))
    (log/info "Ingested data/postnummer.csv in" (- (System/currentTimeMillis) start-ms) "ms."))

  (let [start-ms (System/currentTimeMillis)
        tilsyn-file (io/file "data/tilsyn.csv")]
    (log/info "Ingesting data/tilsyn.csv ...")
    (tilsyn/transact (:datomic/conn powerpack)
                     tilsyn-file
                     (read-string (slurp (io/file "data/ikke-omfattet.edn"))))
    @(d/transact (:datomic/conn powerpack)
                 [{:page/uri "/api/tilsyn.csv"
                   :page/kind :page.kind/csv
                   :page/body (slurp tilsyn-file)}])
    (log/info "Ingested data/tilsyn.csv in" (- (System/currentTimeMillis) start-ms) "ms."))

  (let [start-ms (System/currentTimeMillis)
        vurderinger-file (io/file "data/vurderinger.csv")]
    (log/info "Ingesting data/vurderinger.csv ...")
    (vurderinger/transact (:datomic/conn powerpack) vurderinger-file)
    @(d/transact (:datomic/conn powerpack)
                 [{:page/uri "/api/vurderinger.csv"
                   :page/kind :page.kind/csv
                   :page/body (slurp vurderinger-file)}])
    (log/info "Ingested data/vurderinger.csv in" (- start-ms start-ms) "ms.")))
