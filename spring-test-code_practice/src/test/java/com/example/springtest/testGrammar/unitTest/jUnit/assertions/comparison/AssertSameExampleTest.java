package com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.helper.Greeting;
import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.helper.GreetingWithEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName : com.example.springtest.jUnit.assertions
 * fileName : AssertSameExampleTest
 * author : taeil
 * date : 5/16/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/16/24        taeil                   최초생성
 */
public class AssertSameExampleTest {

    @Test
    void test1() {
        var expected = new GreetingWithEquals("hello");
        var actual = new GreetingWithEquals("hello");
        assertNotSame(expected, actual);
    }

    @Test
    void test2() {
        Greeting expected = new Greeting("hello");
        Greeting actual = expected;
        assertSame(expected, actual);
    }

}