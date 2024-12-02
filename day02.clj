(use 'clojure.string)

(def input (map (fn [x] (read-string (str \[ x \]))) (split-lines (slurp "input02"))))

(when (< (:minor *clojure-version*) 11)
  (defn abs [x] (if (pos? x) x (- x))))

(defn report-safe-difference? [report]
  (every? #(<= 1 % 3) (map (fn [tuple] (apply (comp abs -) tuple)) (partition 2 1 report))))

(defn report-ascending-or-descending [report]
  (or (apply < report) (apply > report)))

(defn report-safe? [report]
  (and (report-safe-difference? report)
       (report-ascending-or-descending report)))

(println "Part 1: "
         (count (filter report-safe? input)))


(defn one-removals [l]
  (for [n (range (count l))]
    (concat (take n l) (drop (inc n) l))))

(one-removals [1 2 3 4])

(println "Part 2: "
         (count (filter (fn [report] (or (report-safe? report)
                                         (some report-safe? (one-removals report)))) input)))
