(require 'clojure.string)
(require 'clojure.set)

(def input (partition-by empty? (clojure.string/split-lines (slurp "input05"))))
(def before (into {} (map (fn [[k v]] [k (set (map second v))]) (group-by first (map (fn [[x xx _ y yy]] [(read-string (str x xx)) (read-string (str y yy))]) (first input))))))

(defn gen-before [pages]
  (loop [m (select-keys before pages)]
    (let [m' (into {} (for [[k v] m] [k (disj (apply clojure.set/union v (map m v)) nil)]))]
      (if (= m m')
        m
        (recur m')))))
  
(def updates (map #(map read-string (clojure.string/split % #",")) (nth input 2)))

(defn update-correct-aux [local-before up]
  (if (empty? up)
    true
    (and
      (empty? (clojure.set/intersection (get local-before (first up)) (set (rest up))))
      (update-correct-aux local-before (rest up)))))

(defn update-correct? [up]
  (update-correct-aux (gen-before up) (reverse up)))

(def correct (filter update-correct? updates))
(println "Part 1:" (apply + (map (fn [x] (nth x (quot (count x) 2))) correct)))


(def incorrect (remove update-correct? updates))
incorrect

(defn sort-pages [updat]
  (let [local-before (gen-before updat)]
    (loop [pages (set updat)
           sorted ()]
      (if (empty? pages)
        (reverse sorted)
        (let [no-dependency (filter (fn [page] (every? (fn [page'] (not (contains? (get local-before page') page))) (disj pages page))) pages)]
          (recur (disj pages (first no-dependency)) (cons (first no-dependency) sorted)))))))

(println "Part 2:" (apply + (map (fn [x] (nth x (quot (count x) 2))) (map sort-pages incorrect))))
