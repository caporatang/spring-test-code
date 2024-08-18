package com.example.springtest.testGrammar.unitTest.jUnit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class BeforeEachAllExampleTest {
    @BeforeAll
    static void beforeAll() { log.info("beforeAll"); }

    @BeforeEach
    void beforeEach() { log.info("beforeEach"); }

    @Test
    void test1() {
        log.info("test1");
        assertTrue(true);
    }

    @Test
    void test2() {
        log.info("test2");
        assertTrue(true);
    }

    @AfterEach
    void afterEach() { log.info("afterEach");}

    @AfterAll
    static void afterAll() { log.info("afterAll"); }

}