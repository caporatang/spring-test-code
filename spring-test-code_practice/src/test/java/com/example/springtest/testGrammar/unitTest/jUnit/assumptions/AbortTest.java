package com.example.springtest.testGrammar.unitTest.jUnit.assumptions;


import static org.junit.jupiter.api.Assumptions.*;

public class AbortTest {
    @TestToIgnore
    void test1() {
        var hasproblem = true;
        if (hasproblem) {
            abort();
        }
    }
}