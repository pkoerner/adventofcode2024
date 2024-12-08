(require 'clojure.string)

(def lines (clojure.string/split-lines (slurp "input08")))
(def width (count (first lines))) 
(def height (count lines))
(def grid (into {} (for [x (range width) y (range height)]
                     [{:x x :y y} (get-in lines [y x])])))


(def antennas (into {} (map (fn [[k v]] [k (map first v)]) (dissoc (group-by second grid) \.))))

(defn in-grid? [{x :x y :y}] (and (< -1 x width) (< -1 y height)))

(defn calc-antinodes [{x1 :x y1 :y :as antenna1} {x2 :x y2 :y :as antenna}]
  (let [x1' (+ x1 (- x1 x2))
        y1' (+ y1 (- y1 y2))
        x2' (+ x2 (- x2 x1))
        y2' (+ y2 (- y2 y1))]
      [{:x x1' :y y1'} {:x x2' :y y2'}]))
 
(def all-antinodes
  (set (filter in-grid? (mapcat (fn [[_ coords]] (apply concat (for [x1 coords x2 coords :when (not= x1 x2)] (calc-antinodes x1 x2)))) antennas))))

(println "Part 1:" (count all-antinodes))

(comment "visualisation"
         (doseq [y (range height)]
           (doseq [x (range width)]
             (let [pos {:x x, :y y}
                   g (get grid pos)]
               (print (cond (and (not= g \.) (get all-antinodes pos)) \?
                            (get all-antinodes pos) \#
                            :else g))))
           (prn)))

(defn calc-next-poss [{x :x y :y} dx dy factor]
  (let [x' (+ x (* factor dx))
        y' (+ y (* factor dy))]
      {:x x' :y y'}))

(defn calc-antinodes2 [{x1 :x y1 :y :as antenna1} {x2 :x y2 :y :as antenna}]
  (let [dir1 (take-while in-grid? (map (partial calc-next-poss antenna1 (- x1 x2) (- y1 y2)) (range)))
        dir2 (take-while in-grid? (map (partial calc-next-poss antenna1 (- x2 x1) (- y2 y1)) (range)))]
    (concat dir1 dir2)))

(def all-antinodes2
  (set (filter in-grid? (mapcat (fn [[_ coords]] (apply concat (for [x1 coords x2 coords :when (not= x1 x2)] (calc-antinodes2 x1 x2)))) antennas))))

(println "Part 2:" (count all-antinodes2))
