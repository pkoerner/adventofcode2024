(require 'clojure.string)
(def lines (clojure.string/split-lines (slurp "input06")))
(def width (count (first lines)))
(def height (count lines))
(def grid (into {} (for [x (range width) y (range height)]
                     [{:x x :y y} (get-in lines [y x])])))
(def guard-pos (ffirst (filter (fn [[pos v]] (= v \^)) grid)))

(def turn-right {:north :east, :east :south, :south :west, :west :north})

(defn in-grid? [pos]
  (and (<= 0 (:x pos) (dec width))
       (<= 0 (:y pos) (dec height))))

(defn next-pos [pos direction]
  (case direction
    :north (update pos :y dec)
    :south (update pos :y inc)
    :west (update pos :x dec)
    :east (update pos :x inc)))

(defn guard-patrol [grid guard-pos]
  (loop [seen #{guard-pos}
         pos guard-pos
         direction :north]
    (let [pos' (next-pos pos direction)]
      (cond (= \# (get grid pos')) (recur seen pos (turn-right direction))
            (not (in-grid? pos')) seen
            :else (recur (conj seen pos') pos' direction)))))

(println "Part 1:" (count (guard-patrol grid guard-pos)))

(defn guard-patrol2 [grid guard-pos]
  (loop [seen #{}
         pos guard-pos
         direction :north]
    (let [pos' (next-pos pos direction)]
      (cond (seen [pos' direction]) :loop
            (= \# (get grid pos')) (recur seen pos (turn-right direction))
            (not (in-grid? pos')) false
            :else (recur (conj seen [pos' direction]) pos' direction)))))


;; Brute Force solution, ~30 sec - 1 min runtime
(println "Part 2"
         (count (filter identity 
                        (for [x (range width) y (range height)
                              :when (= (get grid {:x x :y y}) \.)]
                          (guard-patrol2 (assoc grid {:x x, :y y} \#) guard-pos)))))
