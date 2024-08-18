package com.example.springtest.testGrammar.sliceTest.helper;

import com.example.springtest.testGrammar.sliceTest.controller.GreetingController;
import com.example.springtest.testGrammar.sliceTest.service.GreetingService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * packageName : com.example.springtest.sliceTest.helper
 * fileName : ExampleTestConfig
 * author : taeil
 * date : 6/2/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 6/2/24        taeil                   최초생성
 */
@TestConfiguration
public class ExampleTestConfig {
    @Bean
    GreetingService greetingService() {
        return new GreetingService();
    }

    @Bean
    GreetingController greetingController(GreetingService greetingService) {
        return new GreetingController(greetingService);
    }
}