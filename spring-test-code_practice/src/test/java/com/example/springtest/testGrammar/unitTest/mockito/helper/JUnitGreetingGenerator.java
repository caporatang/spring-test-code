package com.example.springtest.testGrammar.unitTest.mockito.helper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JUnitGreetingGenerator {
    private final String who;

    public String execute() {
        return "hello" + who;
    }
}
