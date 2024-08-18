package com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;

import static org.junit.jupiter.api.Assertions.fail;

public class FailExampleTest {

    @TestToFail
    void test1() {
        var hasProblem = true;
        if (hasProblem) {
            fail();
        }
    }

    @TestToFail
    void test2() {
        var cause = new IllegalStateException();
        fail(cause);
    }

}