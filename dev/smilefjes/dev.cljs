(ns ^:figwheel-hooks smilefjes.dev
  (:require [dataspex.core :as dataspex]
            [smilefjes.ui.main :as smilefjes]))

(dataspex/inspect "App state" smilefjes/store)

(defn ^:after-load main []
  (swap! smilefjes/store assoc :reloaded-at (js/Date.)))

(defonce ^:export kicking-out-the-jams
  (smilefjes/boot
   {:on-render #(dataspex/inspect "Page data" %)}))
