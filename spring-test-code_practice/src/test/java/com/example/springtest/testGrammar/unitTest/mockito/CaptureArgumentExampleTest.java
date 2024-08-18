package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : CaptureArgumentExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class CaptureArgumentExampleTest {
    @Test
    void captureArgument() {
        JUnitGreetingService mocked = mock();

        mocked.greeting("world");
        mocked.greeting("taeil");
        mocked.greeting("earth");

        var captor = ArgumentCaptor.forClass(String.class);
        verify(mocked, times(3)).greeting(captor.capture());

        var expected = List.of("world", "taeil", "earth");
        assertIterableEquals(expected, captor.getAllValues());
    }
}