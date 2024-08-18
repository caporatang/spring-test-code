package com.example.springtest.testGrammar.unitTest.mockito.helper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JUnitGreetingSeparatedService {
    public void hello(String who) {
        String greeting = new JUnitGreetingGenerator(who).execute();
        log.info(greeting);
    }
}
