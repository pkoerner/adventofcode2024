(require 'clojure.string)
(def input (map (into {} (map vector "0123456789" (range 10))) (clojure.string/trim (slurp "input09"))))

(def disk (vec (repeat (reduce + input) 0)))

(defn restore-disk []
  (loop [input input
         free false
         idx 0
         id 0
         disk (transient disk)]
    (if (empty? input)
      (persistent! disk)
      (recur (rest input) 
             (not free) 
             (+ idx (first input))
             (if free (inc id) id)
             (reduce (fn [a e] (assoc! a (+ idx e) (if free \. id))) disk (range (first input)))))))

(restore-disk)

(defn defragment [disk]
  (loop [disk disk
         left 0 
         right (dec (count disk))]
    (cond (>= left right) disk
          (not= \. (get disk left)) (recur disk (inc left) right)
          (= \. (get disk right)) (recur disk left (dec right))
          :else (recur (assoc disk left (get disk right)
                                   right (get disk left))
                       (inc left)
                       (dec right)))))

(defragment (restore-disk))

(defn checksum [disk]
  (reduce + (map (fn [n d] (if (= d \.) 0 (* n d))) (range) disk)))

(println "Part 1:" (checksum (defragment (restore-disk))))

(defn create-freemap [disk]
  (loop [disk disk
         free false
         idx 0
         freemap {}]
    (cond (empty? disk) freemap
          (not free) (recur (rest disk) (not free) (+ idx (first disk)) freemap)
          :else (recur (rest disk) (not free) (+ idx (first disk)) (update freemap (first disk) (fn [v] (if v (conj v idx) [idx])))))))

(create-freemap input)
