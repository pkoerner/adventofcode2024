(require 'clojure.string)
(require 'clojure.set)

(defn safe-read-string [x] ;; for test inputs
  (if (number? (read-string x))
    (read-string x)
    1000))

(def lines (clojure.string/split-lines (slurp "input10")))
(def width (count (first lines))) 
(def height (count lines))
(def grid (into {} (for [x (range width) y (range height)]
                     [{:x x :y y} (safe-read-string (str (get-in lines [y x])))])))

(defn build-trail [pos]
  (let [level (get grid pos)
        neighbours (remove nil? [(update pos :x inc) (update pos :x dec) (update pos :y inc) (update pos :y dec)])]
    (if (= level 9)
      #{pos} 
      (apply clojure.set/union (map build-trail (filter (comp (partial = (inc level)) (partial get grid)) neighbours))))))

(println "Part 1"
         (reduce + (for [x (range width) y (range height)
                         :when (zero? (get grid {:x x :y y}))]
                     (count (build-trail {:x x :y y}))))) 

(defn build-trail2 [pos]
  (let [level (get grid pos)
        neighbours (remove nil? [(update pos :x inc) (update pos :x dec) (update pos :y inc) (update pos :y dec)])]
    (if (= level 9)
      1
      (reduce + (map build-trail2 (filter (comp (partial = (inc level)) (partial get grid)) neighbours))))))

(println "Part 2"
         (reduce + (for [x (range width) y (range height)
                         :when (zero? (get grid {:x x :y y}))]
                     (build-trail2 {:x x :y y}))))
