package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;
import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : VerificationTImeoutTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class VerificationTimeoutTest {
    @Test
    void test1() {
        JUnitGreetingService mocked = mock();

        mocked.hello("world");

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            mocked.hello("world");
            mocked.hello("world");
            mocked.hello("world");
        });

        verify(mocked, times(1)).hello("world");
        var mode = timeout(1000).times(4);
        verify(mocked, mode).hello("world");
    }

    @TestToFail
    void test2() {
        JUnitGreetingService mocked = mock();

        var mode = timeout(1000).atLeastOnce();
        verify(mocked, mode).hello("world");
    }
}