package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : StubbingVoidExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class StubbingVoidExampleTest {
    @Test
    void test1() {
        JUnitGreetingService mocked = mock();

        doThrow(ArithmeticException.class)
                .when(mocked)
                .hello("taeil");

        doNothing()
                .when(mocked)
                .hello("world");

        doReturn("hoi world")
                .when(mocked)
                .greeting("world");

        assertThrows(ArithmeticException.class, () -> {
            mocked.hello("taeil");
        });
        assertDoesNotThrow(() -> mocked.hello("world"));
        assertEquals("hoi world", mocked.greeting("world"));
    }
}


