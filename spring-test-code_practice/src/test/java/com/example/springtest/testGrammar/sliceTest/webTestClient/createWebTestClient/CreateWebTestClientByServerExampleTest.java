package com.example.springtest.testGrammar.sliceTest.webTestClient.createWebTestClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * packageName : com.example.springtest.sliceTest.webTestClient
 * fileName : CreateWebTestClientByServerExampleTest
 * author : taeil
 * date : 6/11/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 6/11/24        taeil                   최초생성
 */
@ExtendWith(SpringExtension.class)
public class CreateWebTestClientByServerExampleTest {

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }
}