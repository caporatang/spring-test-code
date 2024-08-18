package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.internal.util.MockUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : AnnotationExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class AnnotationExampleTest {
    @RequiredArgsConstructor
    private static class GreetingController {
        private final JUnitGreetingService greetingService;
    }

    @Spy
    @InjectMocks
    private GreetingController greetingController;

    @Mock
    private JUnitGreetingService greetingService;

    @Captor
    private ArgumentCaptor<String> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void captureArgument() {
        greetingService.greeting("world");

        verify(greetingService).greeting(captor.capture());
        assertEquals("world", captor.getValue());
    }

    @Test
    void test1() {
        assertNotNull(greetingController.greetingService);

        assertTrue(MockUtil.isSpy(greetingController));
        assertTrue(MockUtil.isMock(
                greetingController.greetingService));
    }
}
