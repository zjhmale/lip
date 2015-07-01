(ns lip.parser.lexer-test
  (:use [lip.parser.lexer.lexer]
        [lip.parser.lexer.listlexer])
  (:require [clojure.test :refer :all]))

(deftest lexer
  (testing "cosume character"
    (is (consume (init "hehe 333")) {:char \h :point 0 :input "hehe 333"})))

(deftest listlexer
  (testing "get-nameseq"
    (is (= (get-nameseq {:char \h, :point 0, :input "hehe 333"})
           {:token   (->ListToken NAME "hehe")
            :lex-map {:char \space, :point 4, :input "hehe 333"}}))
    (is (= (get-nameseq {:char \a, :point 1, :input "[a, b ]"})
           {:token   (->ListToken NAME "a")
            :lex-map {:char \,, :point 2, :input "[a, b ]"}}))
    (is (= (get-nameseq {:char \a, :point 1, :input "[abc, b ]"})
           {:token   (->ListToken NAME "abc")
            :lex-map {:char \,, :point 4, :input "[abc, b ]"}})))
  (testing "is whitespace"
    (is (false? (is-whitespace \a)))
    (is (true? (is-whitespace \space))))
  (testing "is letter"
    (is (true? (is-letter {:char \a, :point 1, :input "[a, b ]"}))))
  (testing "ignore whitespace"
    (is (= (ignore-whitespace {:char \space, :point 0, :input "    333"})
           {:char \3, :point 4, :input "    333"})))
  (testing "get next token"
    (is (= (next-token {:char \[, :point 0, :input "[a, b ]"})
           {:token   (->ListToken LBRACK "[")
            :lex-map {:char \a, :point 1, :input "[a, b ]"}}))
    (is (= (next-token {:char \a, :point 1, :input "[a, b ]"})
           {:token   (->ListToken NAME "a")
            :lex-map {:char \,, :point 2, :input "[a, b ]"}}))
    (is (= (next-token (:lex-map (next-token (:lex-map (next-token (:lex-map (next-token (:lex-map (next-token (init "[abc, b ]"))))))))))
           {:token   (->ListToken RBRACK "]")
            :lex-map {:char :EOF, :point 9, :input "[abc, b ]"}}))
    (is (= (next-token {:char \space, :point 7, :input "[abc, b ]"})
           {:token   (->ListToken RBRACK "]")
            :lex-map {:char :EOF, :point 9, :input "[abc, b ]"}}))))






