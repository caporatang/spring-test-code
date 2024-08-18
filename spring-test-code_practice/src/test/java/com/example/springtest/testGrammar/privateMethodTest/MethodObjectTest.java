package com.example.springtest.testGrammar.privateMethodTest;

import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodObjectTest {
    @Test
    void test1() {
        String who = "world";
        JUnitGreetingGenerator greetingGenerator = new JUnitGreetingGenerator(who);

        String expected = "hello " + who;
        assertEquals(expected, greetingGenerator.execute());
    }
}
