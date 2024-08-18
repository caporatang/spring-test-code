package com.example.springtest.howToTest.calculator;

import com.example.springtest.incorrectAnswerNote.section0.calculator.BadRequestException;
import com.example.springtest.incorrectAnswerNote.section0.calculator.CalculationRequest;
import com.example.springtest.incorrectAnswerNote.section0.calculator.InvalidOperatorException;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * packageName : com.example.springtest.howToTest
 * fileName : CalculationRequestTest
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class CalculationRequestTest {

    @Test
    public void 유효한_숫자를_파싱할_수_있다() {
        // given
        String[] parts = new String[]{"2", "+", "3"};

        // when
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        // then
        assertEquals(2, calculationRequest.getNum1());
        assertEquals("+", calculationRequest.getOperator());
        assertEquals(3, calculationRequest.getNum2());

    }

    @Test
    public void 세자리_숫자가_넘어가는_유효한_숫자를_파싱할_수_있다() {
        // given
        String[] parts = new String[]{"232", "+", "123"};

        // when
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        // then
        assertEquals(232, calculationRequest.getNum1());
        assertEquals("+", calculationRequest.getOperator());
        assertEquals(123, calculationRequest.getNum2());

    }

    @Test
    public void 유효한_길이의_숫자가_들어오지_않으면_에러를_던진다() {
        // given
        String[] parts = new String[]{"232", "+"};

        // when
        // then
        assertThrows(BadRequestException.class, () -> {
            new CalculationRequest(parts);
        });
    }

    @Test
    public void 유효하지_않은_연산자가_들어오면_에러를_던진다() {
        // given
        String[] parts = new String[]{"232", "x", "2"};

        // when
        // then
        assertThrows(InvalidOperatorException.class, () -> { new CalculationRequest(parts);});
    }

    @Test
    public void 유효하지_않은_길이의_연산자가_들어오면_에러를_던진다() {
        // given
        String[] parts = new String[]{"232", "xx", "2"};

        // when
        // then
        assertThrows(InvalidOperatorException.class, () -> {
            new CalculationRequest(parts);
        });
    }

}