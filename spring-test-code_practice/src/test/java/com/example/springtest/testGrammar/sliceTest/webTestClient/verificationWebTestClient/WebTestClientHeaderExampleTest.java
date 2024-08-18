package com.example.springtest.testGrammar.sliceTest.webTestClient.verificationWebTestClient;

import com.example.springtest.testGrammar.sliceTest.controller.GreetingController;
import com.example.springtest.testGrammar.sliceTest.service.GreetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * packageName : com.example.springtest.sliceTest.webTestClient.verificationWebTestClient
 * fileName : WebTestClientHeaderExampleTest
 * author : taeil
 * date : 6/12/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 6/12/24        taeil                   최초생성
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                GreetingController.class,
        }
)
public class WebTestClientHeaderExampleTest {
    @MockBean
    GreetingService mockGreetingService;

    @Autowired
    GreetingController greetingController;

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(
                greetingController
        ).build();
    }

    @Test
    void when_call_then_return_greeting() {
        // given
        String message = "msg";
        when(mockGreetingService.greetingMono(anyString()))
                .thenReturn(Mono.just(message));

        // when
        webTestClient.get()
                .uri("/greeting/header?who=taeil&age=29")
                .exchange()
                .expectHeader()
                .contentType("text/plain;charset=UTF-8")
                .expectHeader()
                .exists("X-WHO")
                .expectHeader()
                .doesNotExist("X-EMAIL")
                .expectHeader()
                .value("X-WHO", header -> {
                    assertEquals("taeil", header);
                })
                .expectHeader()
                .valueEquals("X-AGE", 20L);
    }

}
