(ns smilefjes.pages.search-page
  (:require [datomic-type-extensions.api :as d]
            [smilefjes.link :as link]
            [smilefjes.search-index :as index]
            [smilefjes.ui :as ui]))

(defn get-spisesteder [db]
  (->> (d/q '[:find [?e ...]
              :where
              [?e :tilsynsobjekt/id]]
            db)
       (map #(d/entity db %))))

(defn format-lookup [ctx spisested]
  (let [{:keys [linje1 linje2 postnummer poststed]} (:spisested/adresse spisested)]
    [(link/link-to ctx spisested)
     (:spisested/navn spisested)
     linje1 linje2
     postnummer
     poststed]))

(defn render-index [ctx]
  (let [spisesteder (get-spisesteder (:app/db ctx))]
    {:headers {"content-type" "application/json"}
     :body {:index (->> spisesteder
                        (map-indexed vector)
                        index/build-index)
            :lookup (mapv #(format-lookup ctx %) spisesteder)}}))

(defn render-page [ctx]
  (ui/with-layout ctx
    (ui/header)
    [:div.bg-sommerdag-200
     [:form.max-w-screen-md.px-5.py-28.mx-auto.js-autocomplete {:method :get}
      [:h1.text-xl "Søk etter smilefjes"]
      [:fieldset.mt-1
       {:class [:relative :w-full "min-w-36" :h-10]}
       [:input
        {:class [:shadow :appearance-none :border :rounded :w-full
                 :py-2 :px-3 :text-gray-700 :leading-tight :focus:outline-none
                 :focus:shadow-outline]
         :autocomplete "off"
         :type "search"
         :aria-autocomplete "list"
         :aria-controls "search-autocomplete"
         :aria-haspopup "menu"}]
       [:ol.js-suggestions.px-2.py-2.border.hidden.bg-slate-50
        {:id "search-autocomplete"}]]]]))
