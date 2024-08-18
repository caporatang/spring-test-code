package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : AnyIsAExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class AnyIsAExampleTest {
    @Test
    void test1() {
        JUnitGreetingService mocked = mock();

        when(mocked.greeting(any(String.class)))
                .thenReturn("hi world");

        assertEquals("hi world", mocked.greeting("world"));
    }

    @Test
    void test2() {
        JUnitGreetingService mocked = mock();

        when(mocked.greeting(isA(String.class)))
                .thenReturn("hi world");

        assertEquals("hi world", mocked.greeting("world"));
    }
}
