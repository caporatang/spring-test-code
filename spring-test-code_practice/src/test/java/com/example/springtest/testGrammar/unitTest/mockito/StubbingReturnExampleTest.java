package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : StubbingReturnExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class StubbingReturnExampleTest {

    @Test
    void test1() {
        JUnitGreetingService mocked = mock();

        when(mocked.greeting("world")).thenReturn("hi world");

        assertEquals("hi world", mocked.greeting("world"));
        assertEquals("hi world", mocked.greeting("world"));
    }

    @Test
    void test2() {
        JUnitGreetingService mocked = mock();

        when(mocked.greeting("world"))
                .thenReturn(
                        "hello world",
                        "hoi world",
                            "hi world"
                );

        assertEquals("hello world", mocked.greeting("world"));
        assertEquals("hoi world", mocked.greeting("world"));
        assertEquals("hi world", mocked.greeting("world"));
        assertEquals("hi world", mocked.greeting("world"));
    }

    @Test
    void test3() {
        JUnitGreetingService mocked = mock();

        when(mocked.greeting("world"))
                .thenReturn("hoi world")
                .thenReturn("hi world");

        assertEquals("hoi world", mocked.greeting("world"));
        assertEquals("hi world", mocked.greeting("world"));
    }
}