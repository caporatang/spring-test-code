package com.example.springtest.testGrammar.sliceTest.webTestClient.createWebTestClient;

import com.example.springtest.testGrammar.sliceTest.controller.GreetingController;
import com.example.springtest.testGrammar.sliceTest.service.GreetingService;
import com.example.springtest.testGrammar.sliceTest.webTestClient.helper.GreetingControllerAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * packageName : com.example.springtest.sliceTest.webTestClient
 * fileName : CreateWebTestClientByApplicationContextExampleTest
 * author : taeil
 * date : 6/11/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 6/11/24        taeil                   최초생성
 */
@EnableWebFlux
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                GreetingController.class,
                GreetingControllerAdvice.class
        }
)
public class CreateWebTestClientByApplicationContextExampleTest {
    @MockBean
    GreetingService mockGreetingService;

    @Autowired
    ApplicationContext applicationContext;

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToApplicationContext(
                applicationContext
        ).build();
    }


}