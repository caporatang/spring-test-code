## Assertions
Asssertions는 테스트 케이스의 결과를 검증하는 메서드를 제공하는 클래스다.  

### fail
무조건 테스트 케이스를 실패로 만드는 메서드이며, 인자로 Throwable을 제공할 수 있다.

````java
import com.example.springtest.jUnit.assertions.comparison.annotation.TestToFail;

import static org.junit.jupiter.api.Assertions.fail;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test
public @interface TestToFail {
}

public class FailExampleTest {

    @TestToFail
    void test1() {
        var hasProblem = true;
        if (hasProblem) {
            fail();
        }
    }

    @TestToFail
    void test2() {
        var cause = new IllegalStateException();
        fail(cause);
    }

}
````
test1, test2 모두 fail이 실행되어 테스트가 실패되며, test2는 cause가 제공되어 테스트 에러 로그에 익셉션을 출력한다.
@TestToFail 어노테이션은 실패를 위한 테스트 메서드임을 명시하기 위한 어노테이션이다.  


## assertTrue, assertFalse
주어진 조건이 참인지 거짓인지 검증하는 메서드이며, BooleanSupplier와 lambda를 인자로 전달 가능하다.

````java

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertTrueFalseExample {

    @Test
    void checkTrue() {
        assertTrue(true);
    }

    @Test
    void checkTrueWithSupplier() {
        assertTrue(() -> {
            return true;
        });
    }

    @Test
    void checkFalse() {
        assertFalse(false);
    }
    
}
````
## assertNull, assertNotNull
주어진 객체가 null인지 아닌지를 검증할 수 있는 메서드

````java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AssertNullOrNotExampleTest {
    Object target;

    @Test
    void checkNUll() {
        assertNull(target);
    }

    @Nested
    class WhenTargetIsNotNull {
        @BeforeEach
        void setup() {
            target =1;
        }
    }

    @Test
    void checkNotNull() {
        assertNotNull(target);
    }

}
````
Nested 테스트 클래스를 이용해서 컨텍스를 분리해서 checkNotNull에서 검증할때만 target에 1이라는 값을 할당해서 검증할 수 있다.
단, 위처럼 사용하는 경우에는 다른 Nested 테스트 클래스가 실행되면 target에는 1 이라는 값이 할당된 상태로 테스트가 진행된다.  
의도하지 않게 target에 1이라는 값이 할당되는 셈이다. 저렇게 사용하는 경우 AfterEach 를 사용해서 꼭 값을 초기화를 하던지, 리소스를 해제하고 테스트를 진행하도록 한다.  

## assertEquals, assertNotEquals 
인자로 주어진 2개의 값이 일치하는지 일치하지 않는지 검증한다. 
내부적으로 equals로 비교하며, char, byte, short, int, long, float, double, Object 다양한 타입 비교를 지원한다.    

````java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssertEqualsOrNotExampleTest {

    public class Greeting {
        protected String message;
        public Greeting(String message) {
            this.message = message;
        }
    }

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
````
주어진 2개의 int 타입에 대해서 같은지 다른지를 검증하며, Greeting 클래스는 equals를 별도로 구현하지 않았기 때문에 assertNotEquals에 대한 검증결과는 pass로 넘어가게 된다.  

## assertSame, assertNotSame
주어진 2개의 객체가 같은 참조를 갖는지 검증한다. equals로 비교하지 않고, equals가 true 이더라도 같은 참ㅈ를 갖는게 아니라면 assertSame에 대한 테스트는 실패처리 된다.
````java
static void assertSame(Object expected, Obejct actual, String message) {
        // 내부적으로 eqauls가 아니라 != 로 검증한다.
        // 즉, 같은 '참조'를 갖는지 체크한다.
        if(expected != actual) {
            failNotSame(expected, actual, message);
        }
    }
````

````java
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

    @Test
    void test1() {
        var expected = new GreetingWithEquals("hello");
        var actual = new GreetingWithEquals("hello");
        assertNotSame(expected, actual);
    }

    @Test
    void test2() {
        Greeting expected = new Greeting("hello");
        Greeting actual = expected;
        assertSame(expected, actual);
    }
````
GreetingWithEquals는 equals 메서드를 구현하여 message가 동일하면 equals true를 반환한다. 하지만 assertSame은 참조를 확인하기 때문에 test1 에서는 assertNotSame이 pass되고, test2에서는 같은 참조를 갖는 객체를 비교하여 assertSame이 pass된다.  
간단하게 얘기해서 같은 객체를 참조하고 있는지를 검증할때 assertSame으로 확인하면 좋다.  

## assertInstanceOf
인자로 주어진 객체가 특정 클래스를 구현했는지 검증한다. 클래스가 특정 인터페이스 혹은 다른 클래스를 부모로 상속, 구현했는지도 검증이 가능하다.

````java
import com.example.springtest.jUnit.assertions.comparison.annotation.TestToFail;
import com.example.springtest.jUnit.assertions.comparison.helper.Greeting;
import com.example.springtest.jUnit.assertions.comparison.helper.GreetingWithEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AsssertInstanceOfExampleTest {

    @Test
    void test1() {
        Object obj = new Greeting("hello");
        assertInstanceOf(Greeting.class, obj);
    }

    @Test
    void test2() {
        Object obj = new GreetingWithEquals("hello");
        assertInstanceOf(Greeting.class, obj);
    }

    @TestToFail
    void test3() {
        Object obj = new Greeting("hello");
        assertInstanceOf(GreetingWithEquals.class, obj);
    }
}
````
GreetingWithEquals는 Greeting을 상속한다. 따라서 test2에서는 obj는 GreetingWithEquals 객체이고 Greeting을 구현한 것이 맞기 때문에 테스트는 pass된다.  
test3에서 Greeting 객체는 GreetingWithEquals를 구현한 것이 아니기 때문에 테스트는 실패된다.


## assertArrayEquals
인자로 주어진 2개의 Array에 있는 각각 element가 순서대로 모두 일치하는지 검증한다. 내부적으로 equals로 비교하며,
boolean, char, byte, short, int, long, float, double, Object 여러 타입을 비교를 지원한다.

````java
import com.example.springtest.jUnit.assertions.comparison.annotation.TestToFail;
import com.example.springtest.jUnit.assertions.comparison.helper.Greeting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class assertArrayEquals {
    @Test
    void test1() {
        int[] expected = {1, 2, 3};
        int[] actual = {1, 2, 3};
        assertArrayEquals(expected, actual);
    }

    @TestToFail
    void test2() {
        Object[] expected = {new Greeting("hello")};
        Object[] actual = {new Greeting("hello")};
        assertArrayEquals(expected, actual);
    }
}
````
test2의 경우, Greeting 클래스에는 equals가 구현되어 있지 않고 서로 다른 객체이기 때문에 assertArrayEquals 테스트는 fail 처리된다.  
**assertIterableEquals를 사용해서 list도 비교**할 수 있다

## assertLinesMatch
인자로 주어진 2개의 List 혹은 Stream에 있는 각각 **String** 타입의 element가 순서대로 모두 일치하는지 검증한다.   
내부적으로 1차로 equals로 비교하며, 일치하지 않는다면 2차로 정규식 표현으로 비교한다. 

````java
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;


public class AssertLineMatchExampleTest {

    @Test
    void test1() {
        List<String> expected = List.of("abc", "[a-z]", "[0-9]+");
        List<String> actual = List.of("abc", "abc", "123");
        assertLinesMatch(expected, actual);
    }

    @Test
    void test2() {
        Stream<String> expected = Stream.of("abc", "[a-z]+", "[0-9]+");
        Stream<String> actual = Stream.of("abc", "abc", "123");
        assertLinesMatch(expected, actual);
    }
}
````
정규표현식을 사용하여 List에 있는 String 값이 정규표현식에 만족된다면 테스트가 pass되므로, test1과 test2는 모두 pass된다.


