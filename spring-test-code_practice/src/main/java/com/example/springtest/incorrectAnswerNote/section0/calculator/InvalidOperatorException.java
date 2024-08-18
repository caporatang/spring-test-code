package com.example.springtest.incorrectAnswerNote.section0.calculator;

/**
 * packageName : com.example.springtest.howToTest
 * fileName : InvalidOperatorException
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class InvalidOperatorException extends RuntimeException {

    public InvalidOperatorException() {
        super("Invalid operator, you need to choose one of (+,-,*,/)");
    }



}