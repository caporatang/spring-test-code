package com.example.springtest.howToTest.calculator;

import com.example.springtest.incorrectAnswerNote.section0.calculator.Calculator;
import com.example.springtest.incorrectAnswerNote.section0.calculator.InvalidOperatorException;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;


/**
 * packageName : com.example.springtest.howToTest
 * fileName : CalculatorTest
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class CalculatorTest {

    @Test
    public void 덧셈_연산을_할_수_있다() {
        // given
        long num1 = 2;
        String operator = "+";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        long result = calculator.calculate(num1, operator, num2);

        // then
        assertEquals(5, result);
        assertThat(result).isEqualTo(5);
    }

    @Test
    public void 뺄셈_연산을_할_수_있다() {
        // given
        long num1 = 2;
        String operator = "-";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        long result = calculator.calculate(num1, operator, num2);

        // then
        assertEquals(-1, result);
    }

    @Test
    public void 나눗셈_연산을_할_수_있다() {
        // given
        long num1 = 6;
        String operator = "/";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        long result = calculator.calculate(num1, operator, num2);

        // then
        assertEquals(2, result);
    }


    @Test
    public void 곱셈_연산을_할_수_있다() {
        // given
        long num1 = 2;
        String operator = "*";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        long result = calculator.calculate(num1, operator, num2);

        // then
        assertEquals(6, result);
    }

    @Test
    public void 잘못된_연산자가_요청으로_들어올_경우_에러가_발생한다() {
        // given
        long num1 = 2;
        String operator = "x";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        // then
        assertThrows(InvalidOperatorException.class, () -> {
            calculator.calculate(num1, operator, num2);
        });
    }





}