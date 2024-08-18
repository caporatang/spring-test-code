package com.example.springtest.testGrammar.unitTest.jUnit.conditional_execution;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

@Slf4j
public class DisabledJreExampleTest {

    @DisabledOnJre(JRE.JAVA_11)
    @Test
    void testNotOnJava11() {
        log.info("testNotOnJava8");
    }

    @DisabledOnJre(value = {JRE.JAVA_11, JRE.JAVA_12})
    @Test
    void testNotOnJAva11And12() {
        log.info("testNotOnJAva11And12");
    }

    @DisabledOnJre(JRE.JAVA_10)
    @Test
    void testNotOnJava10() {
        log.info("testNotOnJava10");
    }
}