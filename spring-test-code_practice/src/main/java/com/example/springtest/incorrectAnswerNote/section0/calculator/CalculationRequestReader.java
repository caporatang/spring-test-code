package com.example.springtest.incorrectAnswerNote.section0.calculator;

import java.util.Scanner;

/**
 * packageName : com.example.springtest.howToTest
 * fileName : CalculationRequestReader
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class CalculationRequestReader {

    public CalculationRequest read() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter two numbers and an operator (e.g 1 + 2) : ");

        String result = scanner.nextLine();
        String[] parts = result.split(" ");

        return new CalculationRequest(parts);
    }
}