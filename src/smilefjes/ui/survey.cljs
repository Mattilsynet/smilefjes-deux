(ns smilefjes.ui.survey
  (:require [fontawesome.icons :as icons]))

(def survey-content
  {:title "Vi jobber med å gjøre smilefjesordningen bedre. Vil du hjelpe oss?"
   :body (list
          [:p "Skriv inn hva du liker og hva synes kan bli bedre med ordningen. "
           "Det kan handle om nettsidene, plakaten pả serveringsstedene, "
           "registrering av et spisested eller selve inspeksjonen og oppfolgingen "
           "i forbindelse med dette."]
          [:p.font-bold
           "Obs! Har du bekymringsmeldinger om konkrete spisesteder skal det "
           "ikke legges inn her, bruk heller "
           [:a.underline.hover:no-underline {:href "https://mattilsynet.no/varsle/varsle-om-mat-eller-drikke"}
            "bekymringsmelding på mattilsynet.no"] "."])
   :anonymous-title "Du kan være anonym, men fortell oss gjerne om du"
   :questions [{:id :jobber-i-serveringsbransjen
                :text "jobber på/eier serveringssted"}
               {:id :journalist
                :text "er journalist og interessert i smilefjesordningen"}
               {:id :restaurantgjest
                :text "besøker serveringssteder som gjest"}]
   :contact-preamble "Senere vil vi snakke med serveringssteder, media og
   publikum for å lære om deres behov. Kan vi potensielt kontakte deg for en
   kort prat? Du får et gavekort som takk for hjelpen."
   :contact-label "Mailadresse (Valgfritt)"
   :contact-aside "Oppgir du kontaktinfo godtar du at vi händterer og oppbevarer denne informasjonen."
   :button-text "Send inn"})

(defn prepare-toggle [state]
  {:text (:title survey-content)
   :actions [[:action/assoc-in [::expanded?] (not (::expanded? state))]]
   :icon (if (::expanded? state)
           (icons/icon :fontawesome.solid/caret-up)
           (icons/icon :fontawesome.solid/caret-down))})

(defn prepare [state]
  (cond
    (nil? (get-in state [:location :params "survey"]))
    nil

    (::sent? state)
    {:toggle (prepare-toggle state)
     :sections
     [{:title "Takk!"
       :body "Vi setter stor pris på at du tok deg tid til å sende oss noen tanker."
       :button {:text "Lukk"
                :actions [[:action/assoc-in [::sent?] nil]]}}]}

    (not (::completed? state))
    (cond-> {:toggle (prepare-toggle state)}
      (::expanded? state)
      (assoc :sections
             [{:body (:body survey-content)
               :textarea {:value (::feedback-text state)
                          :actions [[:action/assoc-in [::feedback-text] :event/target-value]]}}
              {:title (:anonymous-title survey-content)
               :checkboxes (for [{:keys [id text]} (:questions survey-content)]
                             (let [questions (set (::questions state))
                                   checked? (questions id)]
                               {:text text
                                :icon (if checked?
                                        (icons/icon :fontawesome.solid/square-check)
                                        (icons/icon :fontawesome.regular/square))
                                :actions [[:action/assoc-in [::questions]
                                           (if checked?
                                             (disj questions id)
                                             (conj questions id))]]}))}
              (let [email (not-empty (::email state))
                    valid-email? (or (empty? email) (re-find #".+@.+\..+" email))]
                {:title (:contact-preamble survey-content)
                 :input (cond-> {:label (:contact-label survey-content)
                                 :value (::email state)
                                 :actions (cond-> [[:action/assoc-in [::email] :event/target-value]]
                                            (and valid-email?
                                                 (::email-validation-error state))
                                            (conj [:action/assoc-in [::email-validation-error] nil]))}
                          (::email-validation-error state)
                          (assoc :error (::email-validation-error state)))
                 :button {:text (:button-text survey-content)
                          :actions (if valid-email?
                                     [[:action/assoc-in [::completed?] true]
                                      [:action/assoc-in [::sent?] true]
                                      [:action/send-survey-response
                                       (cond-> {:tilbakemelding/tekst (::feedback-text state)}
                                         (not-empty (::questions state))
                                         (assoc :tilbakemelding/roller (set (::questions state)))

                                         email
                                         (assoc :tilbakemelding/e-postadresse email))]]
                                     [[:action/assoc-in [::email-validation-error]
                                       "Sjekk at e-postadressen er riktig"]])}})]))))

(defn get-freezer-data [state]
  (select-keys state [::completed?]))

(defn render [survey]
  (when survey
    [:div.p-4.border-b.border-granskog-800.bg-gåsunge-200
     [:div.max-w-screen-lg.mx-auto
      (let [{:keys [text actions icon]} (:toggle survey)]
        [:h2.font-medium.items-center.flex.gap-2.cursor-pointer
         (cond-> {:on {:click actions}}
           (:body survey) (assoc :class "text-xl"))
         text
         (icons/render icon {:class :mmm-button-icon})])
      (for [section (:sections survey)]
        [:div.my-8
         (when-let [title (:title section)]
           [:h2.font-medium.mb-2 title])
         (when (:body section)
           [:div.mb-8 (:body section)])
         (when-let [checkboxes (:checkboxes section)]
           (for [{:keys [text icon actions]} checkboxes]
             [:div.flex.gap-2.items-center.cursor-pointer
              {:on {:click actions}}
              (icons/render icon {:class [:w-4 :h-4]})
              text]))
         (when-let [{:keys [value actions]} (:textarea section)]
           [:textarea {:class ["mmm-textarea" "mmm-focusable" "h-8"]
                       :on {:input actions}
                       :value value}])
         (when-let [{:keys [label value actions error]} (:input section)]
           [:div.mb-4
            [:label.block.mb-2 label]
            [:input
             {:replicant/key label
              :class (cond-> ["mmm-input" "mmm-focusable"]
                       error (conj "mmm-error"))
              :type "text"
              :value value
              :on {:input actions}}]
            (when error
              [:p.mmm-error.mt-2 error])])
         (when-let [{:keys [text actions]} (:button section)]
           [:button.mmm-button.mmm-button-inline.mmm-focusable
            {:on {:click actions}}
            text])])]]))
