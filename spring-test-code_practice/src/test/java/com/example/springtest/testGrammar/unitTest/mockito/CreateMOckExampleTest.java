package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : CreateMOckExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class CreateMOckExampleTest {

    @Test
    void createMock() {
        JUnitGreetingService mocked = mock();
        assertInstanceOf(JUnitGreetingService.class, mocked);
    }

    @Test
    void createMock2() {
        var mocked = mock(JUnitGreetingService.class);
        assertInstanceOf(JUnitGreetingService.class, mocked);
    }
}