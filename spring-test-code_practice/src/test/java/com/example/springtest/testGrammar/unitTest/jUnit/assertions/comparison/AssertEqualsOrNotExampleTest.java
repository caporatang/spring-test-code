package com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison;

import com.example.springtest.testGrammar.unitTest.jUnit.assertions.comparison.helper.Greeting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssertEqualsOrNotExampleTest {

    @Test
    void checkEquals(){
        assertEquals(1,1);
    }

    @Test
    void checkNotEquals() {
        assertNotEquals(1,2);
    }

    @Test
    void checkObjectEquals() {
        assertNotEquals(
                new Greeting("hello"), new Greeting("hello")
        );
    }

}