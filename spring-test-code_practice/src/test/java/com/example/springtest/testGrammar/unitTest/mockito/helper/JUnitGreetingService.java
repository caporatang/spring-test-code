package com.example.springtest.testGrammar.unitTest.mockito.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class JUnitGreetingService {

    private Integer count = 100;

    private void setCount(Integer c) {
        this.count = c;
    }

    public void hello(String who) {
        // void 테스트
        String greeting = prepareGreeting(who);
        log.info(greeting);
    }

    public String greeting(String who) {
        // Object(String)을 반환
        return prepareGreeting(who);
    }

    public Mono<String> GreetingMono(String who) {
        // Object(Mono<String>) 을 반환
        return Mono.just(prepareGreeting(who));
    }

    public Integer greetingCount() {
        // primitive type wrapper 반환
        return 100;
    }

    private String prepareGreeting(String who) {
        return "hello " + who;
    }
}