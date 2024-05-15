(ns smilefjes.layout)

(defn mattilsynet-logo [color]
  [:svg.mt-logo {:viewBox "0 0 252 45" :fill "none" :xmlns "http://www.w3.org/2000/svg"}
   [:path {:fill color
           :d "M233.702 30.469c-.823 1.566-2.261 2.364-4.29 2.364-2.747 0-4.914-2.02-5.073-4.716h13.845s.118-.32.118-1.524c0-6.093-3.584-9.866-9.356-9.866-2.392 0-4.752 1-6.472 2.742-1.836 1.855-2.845 4.427-2.845 7.242 0 3.037 1.02 5.632 2.946 7.497 1.781 1.727 4.211 2.679 6.835 2.679 3.95 0 6.488-1.934 7.823-4.175.386-.689.698-1.44.935-2.24h-4.47.004v-.003Zm-4.717-9.808c2.019 0 4.413 1.046 4.57 3.982h-9.095c.256-2.586 2.509-3.982 4.525-3.982ZM145.323 13.827c1.72 0 3.069-1.314 3.069-2.993 0-1.679-1.378-3.032-3.069-3.032a3.023 3.023 0 0 0-3.032 3.032c0 1.672 1.359 2.993 3.032 2.993ZM147.423 17.323h-4.687v19.043h4.687V17.323ZM157.433 8.174h-4.687v28.192h4.687V8.174ZM170.557 24.957l-2.881-.58c-1.146-.226-1.83-.929-1.83-1.875 0-1.16 1.155-2.07 2.628-2.07 2.868 0 3.34 2.281 3.409 2.74l.015.105 3.999-1.134-.014-.084a6.206 6.206 0 0 0-1.817-3.373c-1.355-1.298-3.239-1.958-5.592-1.958-3.893 0-7.061 2.762-7.061 6.159 0 2.78 1.933 4.868 5.17 5.575l2.805.619c1.402.285 2.142.974 2.142 1.992s-.85 2.069-2.747 2.069c-2.564 0-3.626-1.681-3.76-3.12l-.01-.114-4.118 1.134.006.08c.143 1.299.879 2.68 1.97 3.696 1.454 1.35 3.51 2.069 5.946 2.069 4.764 0 7.254-3.079 7.254-6.122 0-3.043-2.012-5.053-5.524-5.81h.009l.001.002ZM209.47 16.802c-2.321 0-4.353 1.047-5.51 2.813v-2.273h-4.585v19.021h4.702v-10.96c0-2.643 1.506-4.35 3.836-4.35 3.303 0 3.796 2.557 3.796 4.08v11.232h4.703V24.36c0-4.662-2.66-7.557-6.942-7.557V16.8v.001ZM123.953 32.265c-.163.032-.9.152-1.538.152-1.623 0-2.316-.676-2.316-2.26V21.14h3.927v-3.79h-3.927v-5.755h-4.271v3c0 1.805-1.029 2.842-2.822 2.842h-1.067v3.704h3.5v9.752c0 3.514 2.21 5.697 5.771 5.697 1.55 0 2.419-.272 2.797-.433l.058-.025v-3.887l-.114.022h.002v-.002ZM138.456 32.265a9.56 9.56 0 0 1-1.537.152c-1.623 0-2.314-.676-2.314-2.26V21.14h3.927v-3.79h-3.927v-5.755h-4.271v3c0 1.805-1.029 2.842-2.822 2.842h-1.067v3.704h3.5v9.752c0 3.514 2.21 5.697 5.772 5.697 1.549 0 2.417-.272 2.796-.433l.059-.025v-3.887l-.115.022v-.002h-.001ZM251.889 32.265a9.664 9.664 0 0 1-1.539.152c-1.625 0-2.318-.676-2.318-2.26V21.14h3.93v-3.79h-3.93v-5.755h-4.27v3c0 1.805-1.031 2.842-2.822 2.842h-1.067v3.704h3.498v9.752c0 3.514 2.214 5.697 5.773 5.697 1.607 0 2.494-.301 2.798-.433l.057-.025v-3.887l-.114.022h.004v-.002ZM180.206 43.656h5.036l12.049-26.333h-5.002l-5.044 11.747-5.365-11.69-.026-.057h-5.319l8.153 16.65-4.482 9.681v.002ZM82.06 8.77l-8.782 20.764-8.872-20.709-.025-.056h-6.39v27.597h4.745v-19.73l8.321 19.604h4.286l8.399-19.682v19.808h4.824V8.77h-6.507.002ZM109.536 36.193h-.006c-.01-.047-.23-1.171-.23-2.994v-9.346c0-2.142-.791-7.125-8.123-7.125-5 0-7.65 3.142-8.03 6.105l4.298.97c.247-1.984 1.68-3.216 3.769-3.216 2.089 0 3.475 1.034 3.475 2.764 0 .462-.136 1.092-1.314 1.257l-4.847.735c-3.62.521-5.782 2.678-5.782 5.772 0 1.527.621 2.957 1.745 4.023 1.21 1.145 2.849 1.75 4.749 1.75 3.31 0 5.023-1.762 5.727-2.76.021 1.317.156 1.997.198 2.113l.022.067h4.372l-.022-.115h-.001Zm-4.848-7.784c0 3.944-2.501 4.771-4.598 4.771-1.827 0-2.66-1.212-2.66-2.339 0-1.785 1.53-2.303 2.44-2.454l4.817-.72h.003v.742h-.002ZM22.31 0C11.745 0 2.9 7.34.595 17.197A24.822 24.822 0 0 1 13.36 20.62c1.036-4.977 5.182-7.415 8.943-7.415 4.037 0 8.476 2.843 9.073 8.29a24.816 24.816 0 0 1 13.005-2.39C42.838 8.304 33.544 0 22.31 0ZM44.608 22.284l-.001-.188c-13.078-1.525-24.756 9.236-24.28 22.386 12.95 1.21 24.35-9.188 24.281-22.198ZM17.335 43.887a24.73 24.73 0 0 1 2.96-11.771C16.581 24.689 8.434 19.826.111 20.163c-1.138 11.215 6.603 21.513 17.225 23.849v-.125Z"}]])

(defn header [& [{:keys [url]}]]
  [:div.border-b.border-granskog-800.relative
   [:div.mt-logo-wrapper
    [:a {:href (or url "/")}
     (mattilsynet-logo "#054449")]]
   [:a.absolute.right-0.top.mr-6
    {:href "/kommune/"
     :class "top-1/2"
     :style {:transform "translateY(-50%)"}}
    "Kommuner"]])

(def footer-section-1
  {:header "Mattilsynet"
   :links [{:text "Om Mattilsynet"
            :href "https://www.mattilsynet.no/om-mattilsynet"}
           {:text "Varsle oss"
            :href "https://www.mattilsynet.no/varsle/varsle-om-mat-eller-drikke"}
           {:text "Driver du restaurant?"
            :href "https://www.mattilsynet.no/mat-og-drikke/matservering/smilefjes-tilsyn"}]})

(def footer-section-2
  {:header "Om nettstedet"
   :links [{:text "Om Smilefjes"
            :href "https://www.mattilsynet.no/mat-og-drikke/forbrukere/smilefjesordningen"}
           {:text "Personvernerklæring"
            :href "https://www.mattilsynet.no/om-mattilsynet/personvernerklaering-og-informasjonskapsler"}
           {:text "Tilgjengelighetserklæring"
            :href "https://uustatus.no/nn/erklaringer/publisert/6f6c62f4-caa7-413c-a446-a573a10c243c"}
           {:text "API for smilefjesdata"
            :href "https://data.norge.no/datasets/288aa74c-e3d3-492e-9ede-e71503b3bfd9"}]})

(def footer-section-3
  {:header "Kontakt"
   :links [{:text "Stensberggata 27"}
           {:text "0170 OSLO"}
           {:text "postmottak@mattilsynet.no"
            :href "mailto:postmottak@mattilsynet.no"}]})

(defn footer-section [{:keys [header links]}]
  [:div.pb-14
   [:h3.text-lg.mb-4 header]
   (for [link links]
     [:div.text-sm.mb-1
      (if (:href link)
        [:a.underline.hover:no-underline {:href (:href link)}
         (:text link)]
        (:text link))])])

(defn footer []
  [:div.bg-granskog-800.py-14
   [:div.max-w-screen-lg.p-4.mx-auto
    [:div.flex.sm:block.justify-center
     [:div.text-white.sm:flex.justify-between
      (footer-section footer-section-1)
      (footer-section footer-section-2)
      (footer-section footer-section-3)]]
    [:div.flex.justify-center
     [:div [:a {:href "/"} (mattilsynet-logo "white")]]]]])

(defn get-standard-head-elements [ctx page]
  (list
   [:title (:page/title page)]
   [:link {:rel "apple-touch-icon" :sizes "180x180" :href "https://www.mattilsynet.no/apple-touch-icon.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "https://www.mattilsynet.no/favicon-32x32.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "https://www.mattilsynet.no/favicon-16x16.png"}]))

(defn get-tracking-pixel [ctx]
  (when-let [site-id (:matomo/site-id ctx)]
    [:img {:data-src (str "https://mattilsynet.matomo.cloud/matomo.php?idsite="
                          site-id
                          "&rec=1"
                          "&url={url}"
                          "&action_name={title}"
                          "&ua={ua}"
                          "&urlref={referrer}")
           :id "smilefjes-tracking-pixel"
           :style "border:0"
           :alt ""}]))

(defn with-layout [ctx page & body]
  [:html
   [:head
    (get-standard-head-elements ctx page)
    (:head-extras ctx)]
   [:body
    [:div.min-h-screen.flex.flex-col.justify-between
     [:div body]
     (footer)]
    (get-tracking-pixel ctx)]])
