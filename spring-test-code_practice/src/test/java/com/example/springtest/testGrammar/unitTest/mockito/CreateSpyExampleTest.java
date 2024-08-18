package com.example.springtest.testGrammar.unitTest.mockito;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;
import com.example.springtest.testGrammar.unitTest.mockito.helper.JUnitGreetingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;

/**
 * packageName : com.example.springtest.unitTest.mockito
 * fileName : CreateSpyExampleTest
 * author : taeil
 * date : 5/26/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/26/24        taeil                   최초생성
 */
public class CreateSpyExampleTest {
    static class Foo {
        Foo(String name) {}
        void bar() {}
    }

    @TestToFail
    void failToCreateSpy() {
        Foo a = spy(Foo.class);
        a.bar();
    }

    @Test
    void createSpy() {
        JUnitGreetingService spy = spy();
        assertNotNull(spy);
    }

    @Test
    void createSpyByObj() {
        JUnitGreetingService obj = new JUnitGreetingService();
        JUnitGreetingService spy = spy(obj);
        assertNotNull(spy);
    }

}