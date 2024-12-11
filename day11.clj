(def numbers (read-string (str \[ (slurp "input11") \])))

(defn simulate-stone [s]
  (cond (zero? s) [1]
        (zero? (mod (count (str s)) 2)) (let [st (str s) c (count st)] [(Long/parseLong (apply str (take (/ c 2) st))) (Long/parseLong (apply str (drop (/ c 2) st)))])
        :else [(* 2024 s)]))

(defn simulate-step [stonemap]
  (apply merge-with + 
         (for [[k v] stonemap]
           (apply merge-with + (map (fn [kk] {kk v}) (simulate-stone k))))))

(defn simulate-steps [stonemap n]
  (if (zero? n)
    stonemap
    (recur (simulate-step stonemap) (dec n))))


(println "Part 1" (reduce + (vals (simulate-steps (frequencies numbers) 25))))

(println "Part 2" (reduce + (vals (simulate-steps (frequencies numbers) 75))))

