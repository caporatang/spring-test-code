package com.example.springtest.incorrectAnswerNote.section0.calculator;

/**
 * packageName : com.example.springtest.howToTest
 * fileName : CalculationRequest
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class CalculationRequest {
    private final long num1;
    private final long num2;
    private final String operator;

    public CalculationRequest(String[] parts) {
        if (parts.length != 3) {
            throw new BadRequestException();
        }

        String operator = parts[1];
        if (parts[1].length() != 1 || isInvalidOperator(operator)) {
            throw new InvalidOperatorException();
        }
        this.num1 = Long.parseLong(parts[0]);
        this.num2 = Long.parseLong(parts[2]);
        this.operator = operator;
    }

    private static boolean isInvalidOperator(String operator) {
        return !operator.equals("+") &&
                !operator.equals("-") &&
                !operator.equals("*") &&
                !operator.equals("/");
    }

    public long getNum1() {
        return num1;
    }

    public long getNum2() {
        return num2;
    }

    public String getOperator() {
        return operator;
    }
}