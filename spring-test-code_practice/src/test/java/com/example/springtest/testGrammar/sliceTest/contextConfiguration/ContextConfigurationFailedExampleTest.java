package com.example.springtest.testGrammar.sliceTest.contextConfiguration;

import com.example.springtest.testGrammar.sliceTest.controller.GreetingController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

/**
 * packageName : com.example.springtest.sliceTest
 * fileName : ContextConfigurationFiailedExampleTest
 * author : taeil
 * date : 6/2/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 6/2/24        taeil                   최초생성
 */

@ExtendWith({SpringExtension.class})
@ContextConfiguration(
        classes = {
                GreetingController.class,
        }
)
public class ContextConfigurationFailedExampleTest {
    @Autowired
    GreetingController greetingController;

    @Test
    void when_request_get_then_return_greeting() {
        // given
        var who = "world";

        // when
        var result = greetingController.greeting(who);

        //then
        var expected = "hello world";
        StepVerifier.create(result)
                .expectNext(expected)
                .verifyComplete();
    }
}