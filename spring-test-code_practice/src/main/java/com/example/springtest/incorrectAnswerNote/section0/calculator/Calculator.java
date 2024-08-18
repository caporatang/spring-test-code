package com.example.springtest.incorrectAnswerNote.section0.calculator;

/**
 * packageName : com.example.springtest.howToTest
 * fileName : Calculator
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class Calculator {

    public long calculate(long num1, String operator, long num2) {
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            default -> throw new InvalidOperatorException();
        };
    }

}