package com.example.springtest.testGrammar.unitTest.reactorTest;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.annotation.TestToFail;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class LastStepExpectCompleteExampleTest {
    @Test
    void test1() {
        var flux = Flux.range(0, 5);

        StepVerifier.create(flux)
                .expectNextCount(5)
                .expectComplete()
                .verify();
    }

    @TestToFail
    void test2() {
        var flux = Flux.error(new IllegalStateException());

        StepVerifier.create(flux)
                .expectComplete()
                .verify();
    }
}
