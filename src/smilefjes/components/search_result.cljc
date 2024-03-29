(ns smilefjes.components.search-result
  (:require [smilefjes.components.autocomplete :as ac]
            [smilefjes.icons :as smil]
            [smilefjes.plakaten :as plakaten]
            [smilefjes.ui.search :as search]))

(defn Result [{:keys [opts href title description zebra? illustrations]}]
  [:li opts
   [:a.py-2.px-4.md:px-2.cursor-pointer.hover:bg-gåsunge-300.flex.items-center
    {:href href
     :class [(when zebra? "bg-gåsunge-200")]}
    [:span.grow.py-2.pr-4
     [:div.underline.hover:no-underline title]
     [:div.text-sm description]]
    [:span.flex.md:gap-4.md:basis-72.basis-12.shrink-0
     (map-indexed
      (fn [idx {:keys [illustration title description]}]
        [:div.p-1.text-center.flex.flex-col.items-center
         (when (< 0 idx)
           {:class "max-md:hidden"})
         [:div.w-8.my-2 {:title title}
          illustration]
         [:div.text-xs description]])
      illustrations)]]])

(defn SearchResult [{:keys [results loading?]}]
  [:div
   (when-not loading?
     [:h2.text-xl.pl-4.md:pl-2.mb-2 (count results) " spisesteder"])
   [:ol
    (if loading?
      ac/loader-skeleton
      (map Result results))]])

(defn prepare-illustration [[karakter date]]
  {:illustration (smil/karakter->smil karakter)
   :title (str "Spisestedet har fått " (plakaten/beskriv-karakter karakter) ".")
   :description (let [[_ year month day] (re-find #"(.*)-(.*)-(.*)" date)]
                  (str day "." month "." (.substring year 2)))})

(defn prepare [state]
  (let [query (get-in state [:location :params "q"])
        results (->> (get-in state [:search :results query])
                     ac/prepare-suggestions)
        smil-n 4]
    {:loading? (search/loading? state)
     :results (for [result results]
                (assoc result :illustrations (map prepare-illustration (take smil-n (:tilsyn result)))))}))

(defn get-boot-actions [state]
  (when-let [query (not-empty (get-in state [:location :params "q"]))]
    [[:action/search query]]))
