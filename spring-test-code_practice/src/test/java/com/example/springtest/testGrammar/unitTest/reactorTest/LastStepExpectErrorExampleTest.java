package com.example.springtest.testGrammar.unitTest.reactorTest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class LastStepExpectErrorExampleTest {
    @Test
    void test1() {
        var flux = Flux.error(new IllegalStateException());
        StepVerifier.create(flux)
                .expectError()
                .verify();
    }

    @Test
    void test2() {
        var flux = Flux.error(new IllegalStateException());
        StepVerifier.create(flux)
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void test3() {
        var message = "custom message";
        var flux = Flux.error(new IllegalStateException(message));
        StepVerifier.create(flux)
                .expectErrorMessage(message)
                .verify();
    }
}
