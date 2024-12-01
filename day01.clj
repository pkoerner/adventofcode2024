(use 'clojure.string)

;; Part 1
(let [l (map (fn [line] (read-string (str \[ line \])))
             (split-lines (slurp "input01")))]
  (def l1 (map first l))
  (def l2 (map second l)))

(when (< (:minor *clojure-version*) 11)
  (defn abs [x] (if (pos? x) x (- x))))

(println "Part 1:" (reduce + (map (comp abs -) (sort l1) (sort l2))))

;; Part 2
(let [freqs (frequencies l2)]
  (println "Part 2:" (reduce + (map (fn [n] (* n (get freqs n 0))) l1))))
