package com.example.springtest.howToTest.calculator;

import com.example.springtest.incorrectAnswerNote.section0.calculator.CalculationRequest;
import com.example.springtest.incorrectAnswerNote.section0.calculator.CalculationRequestReader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;


/**
 * packageName : com.example.springtest.howToTest
 * fileName : CalculationRequestReaderTest
 * author : taeil
 * date : 8/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/17/24        taeil                   최초생성
 */
public class CalculationRequestReaderTest {

    @Test
    public void System_in으로_데이터를_읽어들일_수_있다() {
        //given
        CalculationRequestReader calculationRequestReader = new CalculationRequestReader();

        //when
        System.setIn(new ByteArrayInputStream("2 + 3".getBytes()));
        CalculationRequest result = calculationRequestReader.read();

        //then
        assertEquals(2, result.getNum1());
        assertEquals("+", result.getOperator());
        assertEquals(3, result.getNum2());

    }

}