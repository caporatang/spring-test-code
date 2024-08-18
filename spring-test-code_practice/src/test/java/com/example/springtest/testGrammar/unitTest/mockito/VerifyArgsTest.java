package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : VerifyArgsTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class VerifyArgsTest {
    @Test
    void test1() {
        JUnitGreetingService mocked = mock();

        doReturn("hoi world").when(mocked)
                .greeting("world");

        mocked.hello("world");

        verify(mocked).hello("world");
        verify(mocked).hello(eq("world"));
        verify(mocked).hello(argThat(s -> {
            return s.equals("world");
        }));
        verify(mocked).hello(contains("world"));
    }
}
