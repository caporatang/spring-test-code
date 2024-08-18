package com.example.springtest.testGrammar.sliceTest.webTestClient.helper;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * packageName : com.example.springtest.sliceTest.webTestClient.helper
 * fileName : GreetingControllerAdvice
 * author : taeil
 * date : 6/11/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 6/11/24        taeil                   최초생성
 */
@RestControllerAdvice
public class GreetingControllerAdvice {

    @ExceptionHandler
    public String handleGreetingException(GreetingException e) {
        return "GreetingException";
    }
}