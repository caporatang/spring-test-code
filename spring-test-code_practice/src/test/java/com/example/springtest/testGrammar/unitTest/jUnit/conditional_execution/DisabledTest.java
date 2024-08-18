package com.example.springtest.testGrammar.unitTest.jUnit.conditional_execution;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisabledTest {

    @Test
    @Disabled
    void test1() {
        assertEquals(1,1);
    }

    @Test
    @Disabled("~~ 버그 픽스 때까지 비활성화")
    void test2() {
        assertEquals(2,2);
    }
}