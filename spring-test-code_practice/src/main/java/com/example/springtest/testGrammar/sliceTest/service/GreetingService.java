package com.example.springtest.testGrammar.sliceTest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GreetingService {

    public Mono<String> greetingMono(String who) {
        return Mono.just(prepareGreeting(who));
    }

    private String prepareGreeting(String who) {
        return "hello " + who;
    }
}
