package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : ExecuteMockExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class ExecuteMockExampleTest {

    @Test
    void mockMethods() {
        JUnitGreetingService mocked = mock();

        // do nothing
        mocked.hello("world");

        var actualCount = mocked.greetingCount();
        assertEquals(0, actualCount);

        var actualGreeting = mocked.greeting("world");
        assertNull(actualGreeting);

        var actualMono = mocked.GreetingMono("world");
        assertNull(actualMono);
    }
}