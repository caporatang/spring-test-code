package com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.helper.Greeting;
import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;
import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.helper.GreetingWithEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AsssertInstanceOfExampleTest {

    @Test
    void test1() {
        Object obj = new Greeting("hello");
        assertInstanceOf(Greeting.class, obj);
    }

    @Test
    void test2() {
        Object obj = new GreetingWithEquals("hello");
        assertInstanceOf(Greeting.class, obj);
    }

    @TestToFail
    void test3() {
        Object obj = new Greeting("hello");
        assertInstanceOf(GreetingWithEquals.class, obj);
    }

}