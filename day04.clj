(require 'clojure.string)
 
(def inputs (clojure.string/split-lines (slurp "input04")))
(def width (count (first inputs)))
(def height (count inputs))
(def grid (into {} (for [y (range height) x (range width)] [{:x x :y y} (nth (nth inputs y) x)])))

(println "Part 1:" 
         (count
           (filter #{"XMAS"} 
                   (apply concat (for [x (range width)
                                       y (range height)
                                       :when (= (get grid {:x x :y y}) \X)]
                                   [(str \X (get grid {:x (inc x) :y y} "") (get grid {:x (+ x 2) :y y} "") (get grid {:x (+ x 3) :y y} ""))
                                    (str \X (get grid {:x (dec x) :y y} "") (get grid {:x (- x 2) :y y} "") (get grid {:x (- x 3) :y y} ""))
                                    (str \X (get grid {:x x :y (inc y)} "") (get grid {:x x :y (+ y 2)} "") (get grid {:x x :y (+ y 3)} ""))
                                    (str \X (get grid {:x x :y (dec y)} "") (get grid {:x x :y (- y 2)} "") (get grid {:x x :y (- y 3)} ""))
                                    (str \X (get grid {:x (inc x) :y (dec y)} "") (get grid {:x (+ x 2) :y (- y 2)} "") (get grid {:x (+ x 3) :y (- y 3)} ""))
                                    (str \X (get grid {:x (inc x) :y (inc y)} "") (get grid {:x (+ x 2) :y (+ y 2)} "") (get grid {:x (+ x 3) :y (+ y 3)} ""))
                                    (str \X (get grid {:x (dec x) :y (dec y)} "") (get grid {:x (- x 2) :y (- y 2)} "") (get grid {:x (- x 3) :y (- y 3)} ""))
                                    (str \X (get grid {:x (dec x) :y (inc y)} "") (get grid {:x (- x 2) :y (+ y 2)} "") (get grid {:x (- x 3) :y (+ y 3)} ""))])))))

(println "Part 2:"
         (count
           (filter #{["MS" "MS"] ["MS" "SM"] ["SM" "MS"] ["SM" "SM"]}
            (for [x (range width)
                 y (range height)
                 :when (= (get grid {:x x :y y}) \A)]
             [(str (get grid {:x (inc x) :y (dec y)} "") (get grid {:x (dec x) :y (inc y)} "")) 
              (str (get grid {:x (inc x) :y (inc y)} "") (get grid {:x (dec x) :y (dec y)} ""))]))))
