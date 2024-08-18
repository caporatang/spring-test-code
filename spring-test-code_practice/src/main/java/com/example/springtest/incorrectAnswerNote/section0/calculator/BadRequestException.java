package com.example.springtest.incorrectAnswerNote.section0.calculator;

/**
 * packageName : com.example.springtest.howToTest
 * fileName : BadRequestException
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class BadRequestException extends RuntimeException{

    public BadRequestException() {
        super("Invalid input size");
    }
}