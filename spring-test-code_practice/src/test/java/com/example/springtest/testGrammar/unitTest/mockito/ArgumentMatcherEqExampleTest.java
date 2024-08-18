package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : ArgumentMatcherEqExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class ArgumentMatcherEqExampleTest {
    @Test
    void test1() {
        JUnitGreetingService mocked = mock();

        when(mocked.greeting(eq("world")))
                .thenReturn("hoi world");

        assertEquals("hoi world", mocked.greeting("world"));
        verify(mocked).greeting(anyString());
    }
}
