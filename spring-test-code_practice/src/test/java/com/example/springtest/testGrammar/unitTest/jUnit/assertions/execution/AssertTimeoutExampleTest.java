package com.example.springtest.testGrammar.unitTest.jUnit.assertions.execution;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
/**
 * packageName : com.example.springtest.jUnit.assertions.execution
 * fileName : AssertTimeoutExampleTest
 * author : taeil
 * date : 5/17/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/17/24        taeil                   최초생성
 */
public class AssertTimeoutExampleTest {
    @Test
    void test1() {
        var duration = Duration.ofSeconds(1);
        assertTimeout(duration, () -> {
            Thread.sleep(500);
        });
    }

    @Test
    void test2() {
        var duration = Duration.ofSeconds(1);
        Integer result = assertTimeout(duration, () -> {
            Thread.sleep(500);
            return 1;
        });
        assertEquals(1, result);
    }

    @TestToFail
    void test3() {
        var duration = Duration.ofMillis(500);
        assertTimeout(duration, () -> {
            Thread.sleep(1000);
        });
    }
}

