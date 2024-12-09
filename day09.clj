(require 'clojure.string)
(def input (mapv (into {} (map vector "0123456789" (range 10))) (clojure.string/trim (slurp "input09"))))

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

;; this is far from optimal and has loads of potential for optimisation

(defn get-count [id]
  (get input (* 2 id)))


(defn create-freelist [input]
  (loop [disk input
         free false
         idx 0
         freelist []]
    (cond (empty? disk) freelist
          (not free) (recur (rest disk) (not free) (+ idx (first disk)) freelist)
          :else (recur (rest disk) (not free) (+ idx (first disk)) (conj freelist [(first disk) idx])))))

#_ (create-freelist input)

(defn first-free [freelist width]
  (let [[prefix [[hitwidth hitpos] & tail]] (split-with #(not (>= (first %) width)) freelist)]
    (if hitpos
      [hitpos (doall (concat prefix [[(- hitwidth width) (+ hitpos width)]] tail))]
      [nil prefix])))

#_ (first-free (create-freelist input) 3)

(defn move-block [disk rightend width leftfree]
  (loop [disk disk
         n width
         rightend rightend
         leftfree leftfree]
    (if (zero? n)
      disk
      (recur (assoc disk rightend \. leftfree (get disk rightend)) 
             (dec n)
             (dec rightend)
             (inc leftfree)))))

(defn defragment2 [disk]
  (loop [disk disk
         freelist (create-freelist input)
         right (dec (count disk))]
    (cond (or (<= right 0)) disk
          (= \. (get disk right)) (recur disk freelist (dec right))
          :else (let [id (get disk right)
                      width (get-count id)
                      [hitpos freelist'] (first-free freelist width)]
                  (if (< hitpos (- right width))
                    (recur (move-block disk right width hitpos) freelist' (- right width))
                    (recur disk freelist (- right width)))))))

(println "Part 2:" (checksum (defragment2 (restore-disk))))
