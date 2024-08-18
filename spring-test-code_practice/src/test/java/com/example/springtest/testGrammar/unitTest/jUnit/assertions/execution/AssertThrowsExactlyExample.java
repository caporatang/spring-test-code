package com.example.springtest.testGrammar.unitTest.jUnit.assertions.execution;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AssertThrowsExactlyExample {

    @Test
    void test1() {
        assertThrowsExactly(
                IllegalStateException.class,
                () -> {
                    throw new IllegalStateException();
                }
        );
    }

    @TestToFail
    void test2() {
        assertThrowsExactly(
                RuntimeException.class,
                () -> {
                    throw new IllegalStateException();
                }
        );
    }
}