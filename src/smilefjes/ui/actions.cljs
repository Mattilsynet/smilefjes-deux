(ns smilefjes.ui.actions
  (:require [cljs.reader :as reader]
            [clojure.string :as str]
            [clojure.walk :as walk]
            [smilefjes.ui.search :as search]))

(defn interpolate-event-data [event data]
  (walk/postwalk
   (fn [x]
     (cond
       (= :event/key x)
       (.-key event)

       (= :event/target-value x)
       (some-> event .-target .-value)

       (= :event/event x)
       event

       (= :event/target-file-name x)
       (some-> event .-dataTransfer .-files (aget 0) .-name)

       :else x))
   data))

(defn format-query-string [query-params]
  (when (not-empty query-params)
    (->> (for [[k v] query-params]
           (str k "=" v))
         (str/join "&")
         (str "?"))))

(defmulti perform-action (fn [action args] action))

(defmethod perform-action :default [action args]
  (js/console.error "Cannot perform unknown action" (pr-str action) (pr-str args)))

(defn perform-actions [state actions]
  (->> (remove nil? actions)
       (mapcat
        (fn [[action & args]]
          (println "[perform-actions]" action (pr-str args))
          (case action
            :action/assoc-in
            [{:kind ::assoc-in
              :args args}]

            :action/navigate
            [{:kind ::go-to-location
              :location (first args)}]

            :action/update-location
            (let [[path query-params] args]
              [{:kind ::assoc-in
                :args [[:location] {:params query-params}]}
               {:kind ::replace-state
                :location (str path (format-query-string query-params))}])

            :action/search
            (let [[q] args
                  path [:search :results q]]
              (when-not (get-in state path)
                [{:kind ::assoc-in
                  :args [path (vec (search/search-spisesteder state q))]}]))

            :action/send-survey-response
            [{:kind ::http-post
              :url "/tilbakemelding"
              :body (first args)}]

            :action/prevent-default
            [{:kind ::prevent-default
              :event (first args)}]

            (perform-action action args))))))

(defn assoc-in* [m args]
  (reduce
   (fn [m [path v]]
     (assoc-in m path v))
   m
   (partition 2 args)))

(defn read-file [store {:keys [file path parser]}]
  (let [reader (js/FileReader.)]
    (set! (.-onload reader)
          (fn [event]
            (try
              (let [contents (cond-> (-> event .-target .-result)
                               (re-find #"\.edn$" (.-name file)) reader/read-string
                               parser parser)]
                (swap! store assoc-in (conj path (.-name file)) contents))
              (catch :default e
                (js/alert (.-message e))))))
    (.readAsText reader file)))

(defn http-post [{:keys [url body]}]
  (js/fetch url (clj->js {:method "POST"
                          :body (pr-str body)})))

(defn execute! [store effects]
  (doseq [[kind fx] (->> (remove nil? effects)
                         (group-by :kind))]
    (println "[execute!]" kind)
    (case kind
      ::assoc-in (swap! store assoc-in* (mapcat :args fx))
      ::go-to-location (set! js/window.location (:location (first fx)))
      ::http-post (run! http-post fx)
      ::read-file (doseq [effect fx] (read-file store effect))
      ::replace-state (js/history.replaceState nil nil (:location (first fx)))
      ::prevent-default (doseq [e (map :event fx)] (.preventDefault e)))))
