package com.example.springtest.incorrectAnswerNote.section0.calculator;

/**
 * packageName : com.example.springtest.howToTest
 * fileName : SampleApplication
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class SampleApplication {

    public static void main(String[] args) {
        CalculationRequest calculationRequest = new CalculationRequestReader().read();

        long answer = new Calculator().calculate(
                calculationRequest.getNum1(),
                calculationRequest.getOperator(),
                calculationRequest.getNum2());

        System.out.println(answer);
    }


}