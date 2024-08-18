package com.example.springtest.testGrammar.unitTest.jUnit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * packageName : com.example.springtest.jUnit
 * fileName : NestedExampleTest
 * author : taeil
 * date : 5/16/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/16/24        taeil                   최초생성
 */
@Slf4j
public class NestedExampleTest {

    @Test
    void test1() {
        log.info("test1");
    }

    @Nested
    class Nested1 {
        @Test
        void test2() {
            log.info("test2");
        }
    }

    @Nested
    class Nested2 {
        @Test
        void test3() {
            log.info("test3");
        }
    }


}