package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : VerificationTimesTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class VerificationTimesTest {
    @Test
    void test1() {
        JUnitGreetingService mocked = mock();

        mocked.hello("world");
        mocked.hello("world");
        mocked.hello("world");

        verify(mocked, never()).hello("taeil");
        verify(mocked, times(3)).hello("world");
        verify(mocked, atLeast(3)).hello("world");
        verify(mocked, atLeast(0)).hello("world");
        verify(mocked, atMost(3)).hello("world");
        verify(mocked, atMost(99999)).hello("world");
    }
}