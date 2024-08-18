package com.example.springtest.testGrammar.privateMethodTest;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrivateTestReflectionTest {
    @SneakyThrows
    @Test
    void test1() {
        JUnitGreetingService greetingService = new JUnitGreetingService();

        Method privateMethod = greetingService.getClass()
                .getDeclaredMethod("prepareGreeting", String.class);

        privateMethod.setAccessible(true);

        String greeting = (String)privateMethod.invoke(
                greetingService, "world"
        );

        assertEquals("hello world", greeting);
    }
}
