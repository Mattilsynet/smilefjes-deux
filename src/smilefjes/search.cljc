(ns smilefjes.search
  "This is a way too short and over-simplified implementation of some concepts
  loosely borrowed from Elastic Search. It works on an in-memory index
  represented by a map, and may be suitable to power searches in client-side
  datasets that aren't big enough to require the bells and whistles of a more
  tuned implementation.

  Indexing a document consists of breaking its content into tokens and storing
  them in named indexes. Each named sub-index can use a different stack of
  tokenizers. When querying, you can tokenize the query using the same tools,
  combine different indexes with logical AND/OR, and apply boosts.

  There are tokenizers for words in strings, ngrams and edge ngrams. See
  individual functions for details."
  (:require [clojure.string :as str])
  #?(:clj (:import (java.text Normalizer))))

(def sep-re #"[/\.,_\-\?!\s\n\r\(\)\[\]:]+")

(defn tokenize-lower-case
  "Converts a string to a single lower case token"
  [s]
  [(str/lower-case (str/trim s))])

(defn tokenize-words
  "Converts a string to a sequence of word tokens, removing punctuation."
  [s]
  (filter not-empty (str/split s sep-re)))

(defn tokenize-unique-words
  "Converts a string to a sequence of word tokens, removing punctuation."
  [s]
  (set (tokenize-words s)))

(defn tokenize-numberless [s]
  [(str/replace s #"\d" "")])

(defn tokenize-ngrams
  "Converts a string to ngram tokens. When only one number is passed, only that
  sized ngrams are produced, otherwise, every length ngram from `min-n` to
  `max-n` is produced.

  ```clj
  (tokenize-ngrams 1 2 \"Hello\") ;;=> (\"H\" \"e\" \"l\" \"l\" \"o\"
                                  ;;    \"He\" \"el\" \"ll\" \"lo\")
  ```"
  ([n word]
   (tokenize-ngrams n n word))
  ([min-n max-n word]
   (->> (for [n (range min-n (inc max-n))]
          (->> word
               (partition n 1)
               (map str/join)))
        (apply concat))))

(defn tokenize-edge-ngrams
  "Converts a string to ngram tokens from the beginning of the string.
  When only one number is passed, ngrams of size 1 to `n` are produced,
  otherwise, every length ngram from `min-n` to `max-n` is produced.

  ```clj
  (tokenize-edge-ngrams 1 5 \"Hello\") ;;=> (\"H\" \"He\" \"Hel\" \"Hell\" \"Hello\")
  ```"
  ([n word]
   (tokenize-edge-ngrams 1 n word))
  ([min-n max-n word]
   (for [n (range min-n (inc (min max-n (count word))))]
     (str/join (take n word)))))

(defn tokenize
  "Converts value `x` to tokens with the provided `tokenizers`. `tokenizers` is a
  seq of functions that take a single value and return a seq of tokens. The type
  of value `x` and the produced tokens are arbitrary and up to the user, but
  tokenizers must compose. Built-in tokenizers mostly only work with strings for
  `x` (some accept keywords) and all produce a sequence of strings."
  [x & [tokenizers]]
  (reduce
   (fn [tokens f] (mapcat f tokens))
   (remove nil? (if (coll? x) x [x]))
   (or tokenizers [vector])))

(defn remove-diacritics [s]
  [(-> #?(:clj (Normalizer/normalize s java.text.Normalizer$Form/NFD)
          :cljs (.normalize s "NFD"))
       (str/replace #"[\u0300-\u0309\u030B-\u036F]" "")
       (str/replace #"a\u030A" "å"))])

(def stop-words
  #{"og" "eller" "men" "for" "om" "som" "at" "av" "til" "fra" "med"
    "på" "i" "mv" "el" "by" "mm" "pr" "au" "kg" "vit" "stk" "mnd"})

(defn short? [n token]
  (<= (count token) n))

(defn get-field-syms [field xs]
  (for [[word weight] (into [] (frequencies xs))]
    {:field field :sym word :weight weight}))

(defn filter-tokens [filters tokens]
  (reduce (fn [tokens filter]
            (remove filter tokens)) tokens filters))

(defn get-searchable-address [spisested]
  (->> [(-> spisested :spisested/adresse :poststed)
        (-> spisested :spisested/adresse :linje1)
        (-> spisested :spisested/adresse :linje2)
        (-> spisested :spisested/poststed :poststed/kommune :kommune/navn)]
       (filter not-empty)
       (str/join " ")))

(def word-tokenizers
  [remove-diacritics
   tokenize-lower-case
   tokenize-unique-words])

(def ngram-tokenizers
  [tokenize-numberless
   remove-diacritics
   tokenize-lower-case
   tokenize-unique-words
   (partial tokenize-ngrams 2)])

(def schema
  {:spisestedNavn
   {:f :spisested/navn
    :tokenizers word-tokenizers
    :token-filters [stop-words
                    #(short? 1 %)]}

   :spisestedNavnNgrams
   {:f :spisested/navn
    :tokenizers ngram-tokenizers
    :token-filters [stop-words
                    #(short? 1 %)]}

   :spisestedNavnEdgegrams
   {:f :spisested/navn
    :tokenizers [tokenize-lower-case
                 remove-diacritics
                 (partial tokenize-edge-ngrams 3 10)]}

   :sted
   {:f get-searchable-address
    :tokenizers word-tokenizers
    :token-filters [stop-words
                    #(short? 1 %)]}

   :stedNgrams
   {:f get-searchable-address
    :tokenizers ngram-tokenizers
    :token-filters [stop-words
                    #(short? 1 %)]}

   :postnummer
   {:f (comp :postnummer :spisested/adresse)
    :tokenizers [tokenize-lower-case]}})
