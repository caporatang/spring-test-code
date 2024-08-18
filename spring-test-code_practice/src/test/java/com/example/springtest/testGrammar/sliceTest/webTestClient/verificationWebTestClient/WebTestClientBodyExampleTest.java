package com.example.springtest.testGrammar.sliceTest.webTestClient.verificationWebTestClient;

import com.example.springtest.testGrammar.sliceTest.controller.GreetingController;
import com.example.springtest.testGrammar.sliceTest.service.GreetingService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * packageName : com.example.springtest.sliceTest.webTestClient.verificationWebTestClient
 * fileName : WebTestClientBodyExampleTest
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
public class WebTestClientBodyExampleTest {
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

    @EqualsAndHashCode
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class GreetingResponse {
        private String message;
        private String who;
        private int age;
    }

    @Test
    void when_call_then_return_greeting() {
        // given
        String message = "msg";
        when(mockGreetingService.greetingMono(anyString()))
                .thenReturn(Mono.just(message));

        // when
        var expected = new GreetingResponse(message, "grizz", 20);

        webTestClient.get()
                .uri("/greeting/body?who=taeil&age=29")
                .exchange()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(GreetingResponse.class)
                .isEqualTo(expected)
                .value(greetingResponse -> {
                    assertTrue(greetingResponse.age > 0);
                });
    }
}