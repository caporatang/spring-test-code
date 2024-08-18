package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : StubbinAnswerExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class StubbingAnswerExampleTest {

    @Test
    void test1() {
        JUnitGreetingService mocked = mock();

        when(mocked.greeting(anyString()))
                .thenAnswer(invocation -> {
                    String name = invocation.getArgument(0);
                    if (name.equals("taeil")) {
                        throw new ArithmeticException();
                    }
                    return "hoi " + name;
                });

        assertEquals("hoi world", mocked.greeting("world"));
        assertThrows(ArithmeticException.class, () -> {
            mocked.greeting("taeil");
        });
    }
}