package com.example.springtest.testGrammar.unitTest.jUnit.assertions.execution;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssertThrowsExampleTest {
    @Test
    void test1() {
        assertThrows(
                IllegalStateException.class,
                () -> { throw new IllegalStateException(); }
        );
    }

    @Test
    void test2() {
        assertThrows(
                RuntimeException.class,
                () -> { throw new IllegalStateException(); }
        );
    }

    @Test
    void test3() {
        assertThrows(
                IllegalStateException.class,
                () -> { throw new RuntimeException(); }
        );
    }

}