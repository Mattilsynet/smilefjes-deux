(ns smilefjes.pages.spisested-page
  (:require [smilefjes.icons :as icons]
            [smilefjes.layout :as layout]
            [smilefjes.plakaten :as plakaten]
            [smilefjes.tilsyn :as tilsyn]))

(defn zero-pad [n]
  (if (< n 10) (str "0" n) n))

(defn formater-kort-dato [dato]
  (str (zero-pad (.getDayOfMonth dato)) "."
       (zero-pad (.getMonthValue dato)) "."
       (subs (str (.getYear dato)) 2 4)))

(defn formater-dato [dato]
  (str (zero-pad (.getDayOfMonth dato)) "."
       (zero-pad (.getMonthValue dato)) "."
       (.getYear dato)))

(defn vis-siste-tilsynsresultat [besøk]
  (let [karakter (:tilsynsbesøk/smilefjeskarakter besøk)]
    [:div.bg-white.rounded-md.border.border-granskog-800.w-52.pt-4.pb-7.flex.flex-col.items-center
     [:h2.text-l.flex-1 "Siste tilsynsresultat:"]
     [:div.w-28.my-4 {:title (str "Spisestedet har fått " (plakaten/beskriv-karakter karakter) ".")}
      (icons/karakter->smil karakter)]
     (formater-dato (:tilsynsbesøk/dato besøk))]))

(defn vis-spisested-info [spisested]
  (let [{:keys [linje1 linje2 poststed postnummer]} (:spisested/adresse spisested)]
    [:div
     [:h1.text-3xl (:spisested/navn spisested)]
     (when (seq (:spisested/orgnummer spisested))
       [:div.text-xs.mb-2 "Orgnr. " (:spisested/orgnummer spisested)])
     [:div linje1]
     [:div linje2]
     [:div postnummer " " poststed]]))

(defn vis-mini-tilsynsresultat [besøk]
  (let [karakter (:tilsynsbesøk/smilefjeskarakter besøk)]
    [:div.p-1.text-center.flex.flex-col.items-center.rounded-lg.cursor-pointer.border.border-transparent.hover:border-granskog-800
     {:data-select_element_id (:tilsynsbesøk/id besøk)
      :data-selected_class "mmm-mini-selected"}
     [:div.w-8.my-2 {:title (str "Spisestedet har fått " (plakaten/beskriv-karakter karakter) ".")}
      (icons/karakter->smil karakter)]
     [:div.text-xs (formater-kort-dato (:tilsynsbesøk/dato besøk))]]))

(defn hent-vurderinger-av-hovedområdene [besøk]
  (->> (:tilsynsbesøk/vurderinger besøk)
       (remove (comp :kravpunkt/hovedområde :vurdering/kravpunkt))
       (sort-by (comp :kravpunkt/id :vurdering/kravpunkt))))

(defn hent-vurderinger-for-hovedområde [besøk kravpunkt]
  (->> (:tilsynsbesøk/vurderinger besøk)
       (filter (comp #{kravpunkt} :kravpunkt/hovedområde :vurdering/kravpunkt))
       (sort-by (comp :kravpunkt/id :vurdering/kravpunkt))))

(defn vis-karakter-indikator [karakter]
  (case karakter
    ("0" "1") [:img.w-7.mr-3 {:src "/images/checkmark.svg"}]
    ("2" "3") [:img.w-7.mr-3 {:src "/images/xmark.svg"}]
    [:div.w-7.mr-3]))

(defn hent-vurderingstekst [vurdering forrige-besøk]
  (let [forrige-vurdering (->> (:tilsynsbesøk/vurderinger forrige-besøk)
                               (filter #(= (:vurdering/kravpunkt vurdering)
                                           (:vurdering/kravpunkt %)))
                               first)]
    (if (and (#{"0" "1"} (:vurdering/karakter vurdering))
             (#{"2" "3"} (:vurdering/karakter forrige-vurdering)))
      "Regelverksbruddet som ble funnet ved forrige inspeksjon er fulgt opp og funnet i orden."
      ((-> vurdering :vurdering/kravpunkt :kravpunkt/karakter->tekst)
       (:vurdering/karakter vurdering)))))

(defn vis-vurderingsoversikt [besøk forrige-besøk]
  [:div.border-b-2.border-granskog-800
   (for [hovedvurdering (hent-vurderinger-av-hovedområdene besøk)]
     [:div.border-t-2.border-granskog-800
      [:div.bg-gåsunge-300.py-6.px-4.text-xl.flex.items-center
       [:div (vis-karakter-indikator (:vurdering/karakter hovedvurdering))]
       [:div (:kravpunkt/navn (:vurdering/kravpunkt hovedvurdering))]]
      (let [vurderinger (hent-vurderinger-for-hovedområde besøk (:vurdering/kravpunkt hovedvurdering))]
        (for [vurdering vurderinger]
          (let [irrelevant? (#{"4" "5"} (:vurdering/karakter vurdering))]
            [:div (when irrelevant? {:class "irrelevant-vurdering"})
             [:div.bg-white.py-4.px-4.border-b-2.border-gåsunge-200.flex.items-center
              [:div.shrink-0 (vis-karakter-indikator (:vurdering/karakter vurdering))]
              [:div (when irrelevant?
                      {:class ["opacity-50"]})
               [:div (:kravpunkt/navn (:vurdering/kravpunkt vurdering))]
               [:div.text-xs (hent-vurderingstekst vurdering forrige-besøk)]]]])))])])

(defn checkbox [{:keys [toggle-class label]}]
  [:div.px-5
   [:label.mmm-checkbox {:data-toggle_body_class toggle-class}
    [:input {:type "checkbox"}]
    [:svg.mmm-svg.checkbox-marker
     {:xmlns "http://www.w3.org/2000/svg"
      :viewBox "0 0 24 24"}
     [:rect {:x "0.5"
             :y "0.5"
             :width "23"
             :height "23"
             :rx "3.5"}]
     [:svg {:x 5 :y 5}
      [:path {:d "M1.82609 4.97933L0 7.36562L6.05115 12.5999L14 3.3002L12.078 1.3999L6.06382 8.86295L1.82609 4.97933Z"
              :fill "white"
              :stroke "none"}]]]
    label]])

(defn render [ctx spisested]
  (let [besøkene (tilsyn/get-besøk spisested)]
    (layout/with-layout (assoc ctx :head-extras [:link {:rel "canonical" :href (:page/link spisested)}]) spisested
      (layout/header)
      [:div.bg-lav
       [:div.max-w-screen-md.mx-auto.p-5
        [:div.flex.justify-between.items-center
         (let [kommune (:poststed/kommune (:spisested/poststed spisested))]
           [:div.text-xs
            [:a.hover:underline {:href "/"}
             "Smilefjes"]
            [:span.mx-2 ">"]
            [:a.hover:underline {:href (:page/uri kommune)}
             (:kommune/navn kommune)]])
         [:div.mt-1.basis-52.shrink-0.focus-within:basis-72.transition-basis.hidden.md:block.h-10
          [:div.replicant-root {:data-view "autocomplete-small"}]]]
        [:div.flex.mt-5.items-center
         [:div.flex-1.js-select-element-parent
          (vis-spisested-info spisested)
          [:p.mt-4 "Tilsynsresultater:"]
          [:div.flex.gap-3.md:gap-5
           (map vis-mini-tilsynsresultat (take 4 besøkene))]
          (when-let [resten (seq (drop 4 besøkene))]
            [:div
             [:div.gamle-tilsyn
              (for [besøkene (partition-all 4 resten)]
                [:div.flex.gap-3.md:gap-5
                 (map vis-mini-tilsynsresultat besøkene)])]
             [:div.text-xs.mt-2.vis-gamle-tilsyn-lenke
              [:span.underline.cursor-pointer
               {:data-toggle_body_class "vis-gamle-tilsyn"}
               "Se flere tilsynsresultater"]]
             [:div.text-xs.mt-2.skjul-gamle-tilsyn-lenke
              [:span.underline.cursor-pointer
               {:data-toggle_body_class "vis-gamle-tilsyn"}
               "Se færre tilsynsresultater"]]])]
         [:div.hidden.md:block (vis-siste-tilsynsresultat (first besøkene))]]]]
      [:div.bg-gåsunge-200
       [:div.max-w-screen-md.mx-auto.pb-5.pt-10
        [:h2.text-2xl.px-5 "Vurdering av tilsynet"]
        [:div
         (for [[besøk forrige-besøk] (partition-all 2 1 besøkene)]
           [:div {:class (when (not= besøk (first besøkene)) "hidden")
                  :id (:tilsynsbesøk/id besøk)}
            [:p.my-2.px-5 (plakaten/oppsummer-smilefjeskarakter (:tilsynsbesøk/smilefjeskarakter besøk))]
            [:div.md:px-5.mt-5 (vis-vurderingsoversikt besøk forrige-besøk)]])]
        [:div.md:px-5.my-5 (checkbox {:toggle-class "vis-irrelevavnte-vurderinger"
                                      :label "Vis alle kravpunkter"})]
        [:p.px-5.my-10.text-sm
         "Mattilsynet har kontrollert etterlevelsen av sentrale krav i matlovgivningen. Resultatene baserer seg på observasjonene som ble gjort og de opplysningene som ble gitt under inspeksjonen."]]])))
