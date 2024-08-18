package com.example.springtest.testGrammar.unitTest.jUnit.assertions.execution;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class AssertTimeoutPreemptivelyExampleTest {
    @TestToFail
    void test1() {
        var duration = Duration.ofMillis(500);
        assertTimeoutPreemptively(duration, () -> {
            Thread.sleep(1000);
        });
    }
}