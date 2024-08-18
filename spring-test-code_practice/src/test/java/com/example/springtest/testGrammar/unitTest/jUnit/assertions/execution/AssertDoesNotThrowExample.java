package com.example.springtest.testGrammar.unitTest.jUnit.assertions.execution;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertDoesNotThrowExample {
    @Test
    void test1() {
        assertDoesNotThrow(
                () -> {}
        );
    }

    @Test
    void test2() {
        Integer result = assertDoesNotThrow(
                () -> { return 1; }
        );
        assertEquals(1, result);
    }

    @TestToFail
    void test3() {
        assertDoesNotThrow(
                () -> { throw new IllegalStateException(); }
        );
    }
}
