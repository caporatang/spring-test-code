package com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.helper;

/**
 * packageName : com.example.springtest.jUnit.assertions.comparison.helper
 * fileName : GreetingWithEquals
 * author : taeil
 * date : 5/16/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 5/16/24        taeil                   최초생성
 */
public class GreetingWithEquals extends Greeting {

    public GreetingWithEquals(String message) {
        super(message);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GreetingWithEquals) {
            return ((GreetingWithEquals) obj).message.equals(this.message);
        }
        return false;
    }
}