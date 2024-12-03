(defn parse-instr [[_ n1 n2]] [(read-string n1) (read-string n2)])
(def input (map parse-instr (re-seq #"mul\((\d\d?\d?),(\d\d?\d?)\)" (slurp "input03"))))

(println "Part 1:" (reduce + (map (partial apply *) input)))

(defn remove-donts [keep? instrs]
  (cond (empty? instrs)
          ()
        (= (ffirst instrs) "don't()")
          (recur false (rest instrs))
        (= (ffirst instrs) "do()")
          (recur true (rest instrs))
        (not keep?)
          (recur keep? (rest instrs))
        :otherwise
          (cons (first instrs) (remove-donts keep? (rest instrs)))))

(->> "input03"
     slurp
     (re-seq #"do\(\)|don't\(\)|mul\((\d\d?\d?),(\d\d?\d?)\)" )
     (remove-donts true)
     (map parse-instr)
     (map (partial apply *))
     (reduce +)
     (println "Part 2:"))
 
