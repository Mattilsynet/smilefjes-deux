(ns smilefjes.ui.tracking
  (:require [smilefjes.ui.dom :as dom]))

(defn track [url title]
  (if-let [pixel (js/document.getElementById "smilefjes-tracking-pixel")]
    (let [template (.cloneNode pixel)]
      (set! (.-id template) nil)
      (set! (.-src template)
            (-> (.getAttribute template "data-src")
                (.replace "{url}" (js/encodeURIComponent url))
                (.replace "{title}" (js/encodeURIComponent title))
                (.replace "{ua}" (js/encodeURIComponent js/navigator.userAgent))
                (.replace "{referrer}" (js/encodeURIComponent js/document.referrer))))
      (.appendChild js/document.body template)
      (println "[track]" url title 'referrer js/document.referrer 'UA js/navigator.userAgent))
    (println "[tracking skipped]" url title 'referrer js/document.referrer 'UA js/navigator.userAgent)))

(defn track-page-view []
  (when-not (dom/qs "meta[property='skip-track-page-view']")
    (track (str js/location.pathname js/location.search) js/document.title)))
