package com.example.springtest.testGrammar.sliceTest.contextConfiguration;

import com.example.springtest.testGrammar.sliceTest.controller.GreetingController;
import com.example.springtest.testGrammar.sliceTest.service.GreetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * packageName : com.example.springtest.sliceTest
 * fileName : ContextConfigurationMockBeanExampleTest
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
public class ContextConfigurationMockBeanExampleTest {
    @Autowired
    GreetingController greetingController;

    @MockBean
    GreetingService mockGreetingService;

    @Test
    void when_request_get_then_return_greeting() {
        // given
        var who = "world";
        var message = "msg";

        when(mockGreetingService.greetingMono(anyString()))
                .thenReturn(Mono.just(message));

        // when
        var result = greetingController.greeting(who);

        // then
        StepVerifier.create(result)
                .expectNext(message)
                .verifyComplete();
    }
}