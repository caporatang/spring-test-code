package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : StubbingThrowExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class StubbingThrowExampleTest {
    @Test
    void test1 () {
        JUnitGreetingService mocked = mock();

        when(mocked.greeting("world"))
                .thenThrow(IllegalStateException.class);

        assertThrows(IllegalStateException.class, () -> {
            mocked.greeting("world");
        });
    }

    @Test
    void test2() {
        JUnitGreetingService mocked = mock();
        when(mocked.greeting("world"))
                .thenThrow(
                        new IllegalStateException(),
                        new IllegalArgumentException(),
                        new ArithmeticException()
                );

        assertThrows(IllegalStateException.class, () -> {
            mocked.greeting("world");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            mocked.greeting("world");
        });

        assertThrows(ArithmeticException.class, () -> {
            mocked.greeting("world");
        });
    }
}