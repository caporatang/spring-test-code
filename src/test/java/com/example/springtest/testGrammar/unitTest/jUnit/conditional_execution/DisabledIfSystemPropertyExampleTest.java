package com.example.springtest.testGrammar.unitTest.jUnit.conditional_execution;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

@Slf4j
public class DisabledIfSystemPropertyExampleTest {

    @DisabledIfSystemProperty(
            named = "test.bool",
            matches = "true"
    )
    @Test
    void testIfSystemPropertyIsNotTrue() {
        log.info("testIfSystemPropertyIsNotTrue");
    }

    @DisabledIfSystemProperty(
            named = "os.arch",
            matches = "[a-z1-9]+"
    )
    @Test
    void testIfSystemPropertyNotMatchesRegex() {
        log.info("testIfSystemPropertyNotMatchesRegex");
    }
}