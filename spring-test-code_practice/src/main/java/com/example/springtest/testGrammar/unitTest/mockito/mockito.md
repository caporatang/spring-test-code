# Mockito
Java에서 가장 많이 사용되는 mocking 프레임워크이며, mock, spy, stubbing, verify ..등 여러 기능들을 제공한다.    
Mock 객체는 특정 인터페이스 혹은 클래스의 모든 메서드를 갖는 모의 객체를 뜻하며 **Stubbing**을 통해서 주어진 상황에서 특정 액션을 수행하거나 특정 값을 반환하는 객체를 뜻한다.
  
## Mock 객체 생성
mock static 메서드를 통해서 생성 가능하며, 인자를 넘기지 않거나 mocking 하려는 클래스의 Class를 전달한다. reified를 가변인자로 넘기는것처럼 보이지만 제네릭을 통해서 mocking 하려는 클래스 혹은 인터페이스를 감지하기 위한 트릭이다.
````java
/*
 * @Param reified don't pass any values to it.
 *                It's a trick to detect the class/interface you want to mock
 * */
public static <T> T mock(T... reified) {
    return mock(withSettings(), reified);
}

public static <T> T mock(Class<T> classToMock) { 
    return mock(classToMock, withSettings);
}
````
````java
public class CreateMOckExampleTest {

    @Test
    void createMock() {
        GreetingService mocked = mock();
        assertInstanceOf(GreetingService.class, mocked);
    }

    @Test
    void createMock2() {
        var mocked = mock(GreetingService.class);
        assertInstanceOf(GreetingService.class, mocked);
    }
}
````
mmock()를 호출하여 Mock GreetingService를 생성, 혹은 mock()의 인자로 클래스를 전달하여 생성한다.   
Mock 객체는 mockMaker.createMock를 통해서 생성된다. MockMaker는 인터페이스이며 **InlineByteBuddyMockMaker, InlineDelegateByteBuddyMockMaker, ProxyMockMaker** 등의 구현체를 포함한다.  
일반적으로는 InlineByteBuddyMockMaker를 사용하고 ByteBuddy, java reflection 등을 사용해서 Mock 객체를 생성한다. 이때 ByteBuddy는 ByteCode를 직접 변경 가능하다. 즉, Mock 객체를 만드는것은 Byte코드를 직접 뜯어서 객체를 텅 빈 상태로 만드는것이다. 

## Mock 객체 실행
Mock 객체는 타겟이 되는 인터페이스, 클래스의 모든 메서드를 구현한다. 이때, Stubbing이 없다면 void 메서드는 아무것도 하지 않는다. 그리고 primitive 타입을 반환하는 메서드는 기본값을 반환하며 객체를 반환하는 메서드는 null을 반환한다.  
````java
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GreetingService {
    // 테스트에 사용할 클래스
    
    
    public void hello(String who) {
        // void 테스트
        String greeting = prepareGreeting(who);
        log.info(greeting);
    }

    public String greeting(String who) {
        // Object(String)을 반환
        // null 반환
        return prepareGreeting(who);
    }

    public Mono<String> GreetingMono(String who) {
        // Object(Mono<String>) 을 반환
        // null 반환
        return Mono.just(prepareGreeting(who));
    }

    public Integer greetingCount() {
        // primitive type wrapper 반환
        // 0 반환 
        return 100;
    }

    private String prepareGreeting(String who) {
        return "hello " + who;
    }
}
````

````java
public class ExecuteMockExampleTest {

    @Test
    void mockMethods() {
        GreetingService mocked = mock();

        // do nothing
        mocked.hello("world");

        var actualCount = mocked.greetingCount();
        assertEquals(0, actualCount);

        var actualGreeting = mocked.greeting("world");
        assertNull(actualGreeting);

        var actualMono = mocked.GreetingMono("world");
        assertNull(actualMono);
    }
}
````
GreetingService를 mock 객체로 만들고, 메서드를 하나씩 호출해본 결과는 hello는 아무것도 하지 않고,  
greetingCount는 100대신 0을 반환하며 greeting은 "hello world" 대신 null을 반환하고 greetingMono는 Mono 대신 null이 반환된다.  

# Mock Stubbing
mock 객체의 메서드 동작을 사전에 지정한다. when을 통해서 타겟이 되는 mock 객체의 메서드와 인자를 전달한다. 
인자를 전달하는 것은 mock 객체의 메서들들 호출하는 형태이지만, 실제로 해당 메서드가 호출되는 것이 아니라 실행 정보만 mockito에 전달한다.  
이후 thenReturn, thenThrow, thenAnswer 등을 호출하여 다음 동작을 지정하며 스스로(OngoingStubbing)를 반환하여 체이닝을 지원하고 여러 번 호출시 체이닝 순서대로 차례대로 동작한다.
````java

public static <T> OngoingStubbing<T> when(T methodCall) {
    return MOCKITO_CORE.when(methodCall)    
}

@NotExtensible
public interface OngoingStubbing<T> {
    OngoingStubbing<T> thenReturn(T var1);

    OngoingStubbing<T> thenReturn(T var1, T... var2);

    OngoingStubbing<T> thenThrow(Throwable... var1);

    OngoingStubbing<T> thenThrow(Class<? extends Throwable> var1);

    OngoingStubbing<T> thenThrow(Class<? extends Throwable> var1, Class<? extends Throwable>... var2);

    OngoingStubbing<T> thenCallRealMethod();

    OngoingStubbing<T> thenAnswer(Answer<?> var1);

    OngoingStubbing<T> then(Answer<?> var1);

    <M> M getMock();
}
````
## stubbing thenReturn
thenReturn은 하나 이상의 값을 전달하고 하나 이후부터는 가변인자이기 때문에 없어도 상관없다. 2개 이상의 값이 thenReturn에 주어진다면 여러번 메서드 호출시 순서대로 제공하며 만약 주어진 개수보다 더 많이 호출된다면 **가장 마지막 값이 반복적**으로 제공된다.
````java
public class StubbingReturnExampleTest {

    @Test
    void test1() {
        GreetingService mocked = mock();

        when(mocked.greeting("world")).thenReturn("hi world");

        assertEquals("hi world", mocked.greeting("world"));
        assertEquals("hi world", mocked.greeting("world"));
    }

    @Test
    void test2() {
        GreetingService mocked = mock();

        when(mocked.greeting("world"))
                .thenReturn(
                        "hello world",
                        "hoi world",
                            "hi world"
                );

        assertEquals("hello world", mocked.greeting("world"));
        assertEquals("hoi world", mocked.greeting("world"));
        assertEquals("hi world", mocked.greeting("world"));
        // 마지막 값 반복 호출
        assertEquals("hi world", mocked.greeting("world"));
    }

    @Test
    void test3() {
        GreetingService mocked = mock();

        when(mocked.greeting("world"))
                .thenReturn("hoi world")
                .thenReturn("hi world");

        assertEquals("hoi world", mocked.greeting("world"));
        assertEquals("hi world", mocked.greeting("world"));
    }
}
````
test1은 greeting 메서드와 인자 "world"를 전달하며 thenReturn을 체이닝하여 무조건 "hi world"를 반환하게 설정한다.
test2는 여러 개의 값을 제공하여 순서대로 반환하고 test3에서는 thenReturn을 한번 더 체이닝해서 다른 값을 반환하게 설정한다.

## stubbing thenThrow
thenReturn 과 비슷하게 하나 이상의 Throwable을 전달 가능하고, Throwable Class를 하나 이상 전달할 수 있다.  
````java
public class StubbingThrowExampleTest {
    @Test
    void test1 () {
        GreetingService mocked = mock();

        when(mocked.greeting("world"))
                .thenThrow(IllegalAccessError.class);

        assertThrows(IllegalStateException.class, () -> {
            mocked.greeting("world");
        });
    }

    @Test
    void test2() {
        GreetingService mocked = mock();
        when(mocked.greeting("world"))
                .thenThrow(
                        new IllegalStateException(),
                        new IllegalArgumentException(),
                        new ArithmeticException()
                );

        assertThrows(IllegalStateException.class, () -> {
            mocked.greeting("world");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            mocked.greeting("world");
        });

        assertThrows(ArithmeticException.class, () -> {
            mocked.greeting("world");
        });
    }
}
````
IllegalStateException class를 전달하여 greeting 호출시 exception thorw, thenThrow에 여러 exception 객체들을 전달하여 여러 번 호출시 순서대로 throw 한다.

## stubbing thenAnswer
비교적 간단한 케이스에 thenThrow를 사용하고, 복잡한 케이스에는 thenAnswer를 사용하는 것이 좋다.  
mock 객체의 메서드를 직접 구현하며 mock 객체의 메서드가 실행될떄마다 Answer의 answer이 실행되며 Exception을 throw할 수도 있다.  
InvocationOnMock 객체를 인자로 전달하고 반환값을 mock 객체 메서드의 결과로 사용한다.  
getMock : Mock 객체 반환  
getMethod : 실행되는 메서드를 reflection method 형태로 반환한다.  
getArgument(s) : 메서드에 전달된 인자를 반환하며, 특정 index 값에 접근할 수 있다.  
````java
public interface Answer<T> {
    // Answer 안에 
    // answer 이라는 하나의 메서드만 가지고 있기 떄문에
    // 함수형 인터페이스랑 비슷한 형태를 갖는다. 
    T answer(InvocationOnMock invocation) throws Throwable;
}

@NotExtensible
public interface InvocationOnMock extends Serializable {
    Object getMock();

    Method getMethod();

    Object[] getRawArguments();

    Object[] getArguments();

    <T> T getArgument(int var1);

    <T> T getArgument(int var1, Class<T> var2);

    Object callRealMethod() throws Throwable;
}
````
````java
public class StubbingAnswerExampleTest {

    @Test
    void test1() {
        GreetingService mocked = mock();

        when(mocked.greeting(anyString()))
                .thenAnswer(invocation -> {
                    String name = invocation.getArgument(0);
                    if (name.equals("taeil")) {
                        throw new ArithmeticException();
                    }
                    return "hoi " + name;
                });

        assertEquals("hoi world", mocked.greeting("world"));
        assertThrows(ArithmeticException.class, () -> {
            mocked.greeting("taeil");
        });
    }
}
````
getArgument를 통해서 특정 index의 인자에 접근하고, 만약 조건을 충족한다면 ArithmeticException을 throw, 아니라면 값을 생성하여 반환하고 anyString을 통해서 특정값이 아니더라도 포괄적으로 수용이 가능하다.  
이렇게 thenAnswer를 사용하면 다양한 조건에 맞춰서 mocking을 할 수 있고, 조건도 다양하게 설정해서 mocking을 할수도 있다.  

## Stubbing do*
void 메서드를 stubbing 해야한다면, 기존의 when() 메서드는 void 메서드를 인자로 받을 수 없다.   
do* static 메서드는 **Stubber**를 반환하게 되는데, Stubber는 when을 통해서 mock 객체를 인자로 받을 수 있으며, when 호출 이후 체이닝으로 stubbing 하려는 void 메서드를 호출한다. 꼭 void 메서드가 아니여도 값을 반환하는 메서드도 do* 사용이 가능하며 이 경우 다른 기능보다는 스타일의 차이라고 보면된다.
````java
public static Stubber doThrow(Throwable... toBeThrown) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown);
}

public static Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown);
}

public static Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... toBeThrownNext) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown, toBeThrownNext);
}

public static Stubber doCallRealMethod() {
        return MOCKITO_CORE.stubber().doCallRealMethod();
}

public static Stubber doAnswer(Answer answer) {
        return MOCKITO_CORE.stubber().doAnswer(answer);
}

public static Stubber doNothing() {
        return MOCKITO_CORE.stubber().doNothing();
}

public static Stubber doReturn(Object toBeReturned) {
        return MOCKITO_CORE.stubber().doReturn(toBeReturned);
}

public static Stubber doReturn(Object toBeReturned, Object... toBeReturnedNext) {
        return MOCKITO_CORE.stubber().doReturn(toBeReturned, toBeReturnedNext);
}

````
````java
public class StubbingVoidExampleTest {
    @Test
    void test1() {
        GreetingService mocked = mock();

        doThrow(ArithmeticException.class)
                .when(mocked)
                .hello("taeil");

        doNothing()
                .when(mocked)
                .hello("world");

        doReturn("hoi world")
                .when(mocked)
                .greeting("world");

        assertThrows(ArithmeticException.class, () -> {
            mocked.hello("taeil");
        });
        assertDoesNotThrow(() -> mocked.hello("world"));
        assertEquals("hoi world", mocked.greeting("world"));
    }
}
````
인자값에 따라 다르게 stubbing 할수있고, world라면 doNothing, taeil이라면 doThorw를 실행한다.  
greeting은 반환값을 갖고 있지만 doReturn을 통해서 stubbing이 가능하다. 


# Mockito ArgumentMatchers
ArgumentMatchers를 통해서 stubbingg을 할때 타겟이 되는 인자의 조건을 세밀하게 설정할 수 있다.

## ArgumentMatchers Eq
기존에는 ArgumentMatchers 대신 값을 직접 지정하여 사용했고 이는 eq ArgumentMatchers를 사용하는것과 동일하다. 제대로 인자가 전달되었는지 검증하는 경우에도 충족해야 하는 인자의 조건을 설정할 수 있다.    
````java
public class ArgumentMatcherEqExampleTest {
    @Test
    void test1() {
        GreetingService mocked = mock();

        when(mocked.greeting(eq("world")))
                .thenReturn("hoi world");

        assertEquals("hoi world", mocked.greeting("world"));
        // 검증 단계에서도 사용가능하다.
        verify(mocked).greeting(anyString());
    }
}
````

## ArgumentMatchers any, isA
해당 Class를 갖는다면 어떤 값을 갖더라도 허용하는 메서드이며, any 혹은 isA 사용이 가능하다. 이 둘은 동작이 동일하다.  
기본적으로 null은 허용하지 않으며 nullable은 null을 허용한다.  
````java
public class AnyIsAExampleTest {
    @Test
    void test1() {
        GreetingService mocked = mock();

        when(mocked.greeting(any(String.class)))
                .thenReturn("hi world");

        assertEquals("hi world", mocked.greeting("world"));
    }

    @Test
    void test2() {
        GreetingService mocked = mock();

        when(mocked.greeting(isA(String.class)))
                .thenReturn("hi world");

        assertEquals("hi world", mocked.greeting("world"));
    }
}
````

## ArgumentMatchers any*
any와 isA가 일반적인 객체 연산에 대한 지원이라면, any*는 primitive 타입이라면 어떤 값을 갖더라도 허용된다. boolean, byte, char, short, int, long, float, double 타입을 지원한다.
꼭 primitive타입이 아니라도 이미 구현되어 있는 타입도 있다. String, List, Set, Map, Collection, Iterable 등도 같이 지원한다.    
예를들어 anyInt 를 사용한다면, 어떤 특정 값이 와도 상관없으니 int 타입이면 된다 라는 뜻이다.   

## ArgumentMatchers eq*
any와 반대로 equals를 통해서 정확히 값이 일치하는 경우에만 stubbing한다. 지원하는 타입은 boolean, byte, char, short,int,long, float, double, **Object**

## ArgumentMatchers same
eq을 사용해서 Object를 비교하게 되면 Equals를 사용해서 비교하는데, 동일한 참조값을 갖는지 확인하고 싶을떄는 same을 사용한다.  

## ArgumentMatchers String
ArgumentMatchers는 String과 관련된 다양한 helper 검증 메서드를 지원한다.  
contains : 인자가 특정 substring을 포함하고 있는지 검증    
matches : 인자가 특정 정규표현식을 만족하는지 검증, pattern도 전달 가능  
endsWith : 인자가 특정 suffix로 종료되는지 검증  
startsWith : 인자가 특정 prefix로 시작하는지 검증    

# Mockito verify (검증)
mock객체를 만들어서 stubbing을 설정해서 어떻게 동작하고, 결과를 반환하는지 확인이 가능하다. 반환하는 결과는 JUnit을 사용해서 직접적인 검증이 가능하지만, 어려운 케이스들 몇번이나 호출되었는지, 특정 시간동안 몇 회 실행되었는지 이러한 케이스들을 위해 mockito에서는 검증 방법을 제공한다.  
verify 메서드를 사용하여 메서드 실행 횟수, 메서드에 제공된 인자와 관련된 정보를 얻을 수 있고, VerificationMode를 통해서 횟수를 설정할 수 있고 VerificationMode가 주어지지 않는다면 default는 times(1)로 설정된다.  
````java
public static <T> T verify(T mock) {
        return MOCKITO_CORE.verify(mock, times(1));
    }

    public static <T> T verify(T mock, VerificationMode mode) {
        return MOCKITO_CORE.verify(mock, mode);
    }
````

## Mock 객체 호출 횟수 검증
````java
public static VerificationMode atLeastOnce() {
        return atLeast(1);
}

public static VerificationMode never() {
        return times(0);
}

public static Times times(int wantedNumberOfInvocations) {
        return new Times(wantedNumberOfInvocations);
}

public static VerificationMode atLeast(int minNumberOfInvocations) {
        return new AtLeast(minNumberOfInvocations);
}

public static VerificationMode atMostOnce() {
        return atMost(1);
}

public static VerificationMode atMost(int maxNumberOfInvocations) {
        return new AtMost(maxNumberOfInvocations);
}
````
times : 인자로 주어진 값과 실제 실행 횟수가 동일한지 검증한다.    
never : times(0)과 동일하다. 즉, 한번도 실행되지 않았는지 검증한다.    
atLeast : 실제 실행 횟수가 주어진 값보다 크거나 같은지 검증한다 -> atLeast가 3이라면 적어도 3번 이상 실행 여부를 체크한다.  
atLeastOnce : atLeast(1)과 동일
atMost : 실제 실행 횟수가 주어진 값보다 작거나 같은지 검증한다 -> atMost가 3이라면 3번 **이하** 실행 여부를 체크한다.  
atMostOnce : atMost(1)과 동일하다.    

````java
public static VerificationWithTimeout timeout(long millis) {
        return new Timeout(millis, VerificationModeFactory.times(1));
}

public interface VerificationWithTimeout extends VerificationMode {
    VerificationMode times(int var1);

    VerificationMode atLeastOnce();

    VerificationMode atLeast(int var1);

    VerificationMode only();
}
````
tiemout : 주어진 millis 동안 호출이 발생하는지 검증한다. verify가 실행된 시점을 기준으로 메서드 호출을 기다리며 **비동기 non-blocking하게 동작하는 메서드 호출을 감지**하기 위해 사용한다.  
timeout은 VerificationWithTimeout을 반환하고 VerificationWithTimeout은 times와 atLeastOnce, atLeast, Only를 메서드로 제공하여 체닝이 가능하다. 따로 체이닝하지 않는다면 times(1)로 설정된다.  

````java
public class VerificationTimesTest {
    @Test
    void test1() {
        GreetingService mocked = mock();
        
        mocked.hello("world");
        mocked.hello("world");
        mocked.hello("world");

        verify(mocked, never()).hello("taeil");
        verify(mocked, times(3)).hello("world");
        verify(mocked, atLeast(3)).hello("world");
        verify(mocked, atLeast(0)).hello("world");
        verify(mocked, atMost(3)).hello("world");
        verify(mocked, atMost(99999)).hello("world");
    }
}
````
hello가 "world"와 함께 호출된 횟수는 3번이므로 times(3) 조건이 충족되고 3번 이상 atLeast(3)과 0번 이상 atLeast(0) 조건 충족되며, 3번 이하 atMost(3)과 99999번 이하 atMost(99999)또한 조건에 충족된다.    
taeil은 한번도 호출된적이 없기 떄문에 never, times(0)이 충족된다.  


````java
public class VerificationTimeoutTest {
    @Test
    void test1() {
        GreetingService mocked = mock();

        mocked.hello("world");

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            mocked.hello("world");
            mocked.hello("world");
            mocked.hello("world");
        });

        verify(mocked, times(1)).hello("world");
        var mode = timeout(1000).times(4);
        verify(mocked, mode).hello("world");
    }

    @TestToFail
    void test2() {
        GreetingService mocked = mock();

        var mode = timeout(1000).atLeastOnce();
        verify(mocked, mode).hello("world");
    }
}
````
timeout 또한 마찬가지로 검증이 가능한데, 비동기적으로 동작하는 코드에 대한 검증이 가능하다.  
hello를 한번 호출 이후 "world"가 한번 호출 됐는지 검증하고, 1000ms가 지나고 나서 총 호출 횟수가 4번이 되었는지 체크한다.  
test2에서는 1초 대기하는 동안 한번도 호출되지 않았고 최소 1번 이상 호출이 충족되지 않았기 때문에 실패한다.  

## Mock 객체 인자 검증
verify 호출 후 검증하려고 하는 메서드를 호출하고 기대하는 인자를 전달한다. ArgumentMatchers를 사용하여 다양한 조건을 확인할 수 있다.  
````java
public class VerifyArgsTest {
    @Test
    void test1() {
        GreetingService mocked = mock();

        doReturn("hoi world").when(mocked)
                .greeting("world");

        mocked.hello("world");

        verify(mocked).hello("world");
        verify(mocked).hello(eq("world"));
        verify(mocked).hello(argThat(s -> {
            return s.equals("world");
        }));
        verify(mocked).hello(contains("world"));
    }
}
````
verify 호출 후 검증하려고 하는 메서드를 호춣사고 기대하는 인자를 전달한다.  
ArgumentMatchers를 사용하여 다양한 조건을 확인 가능하며 값 그대로 eq, argThat을 통해서 조건을 충족하는지 확인한다. contains를 통해서 인자가 world를 포함하는지 검증 가능하다.  

# Mock 객체 인자 capture
인자를 단순히 검증하는 것으로 끝내지 않고 캡쳐해서 검증 메서드 바깥에서 테스트하거나 사용하고 싶은 경우에 사용한다.  
ArgumentCaptor의 forClass를 사용해서 저장공간을 만들고 capture 메서드로 저장한다. 메서드가 여러 번 호출되었다면, getAllValues를 통해서 호출된 모든 인자에 접근 가능하다.  
````java
@CheckReturnValue
public class ArgumentCaptor<T> {
    private final CapturingMatcher<T> capturingMatcher;
    private final Class<? extends T> clazz;

    private ArgumentCaptor(Class<? extends T> clazz) {
        this.clazz = clazz;
        this.capturingMatcher = new CapturingMatcher(clazz);
    }

    public T capture() {
        T ignored = Mockito.argThat(this.capturingMatcher);
        return Primitives.defaultValue(this.clazz);
    }

    public T getValue() {
        return this.capturingMatcher.getLastValue();
    }

    public List<T> getAllValues() {
        return this.capturingMatcher.getAllValues();
    }

    public static <U, S extends U> ArgumentCaptor<U> forClass(Class<S> clazz) {
        return new ArgumentCaptor(clazz);
    }
}
````
````java
public class CaptureArgumentExampleTest {
    @Test
    void captureArgument() {
        GreetingService mocked = mock();

        mocked.greeting("world");
        mocked.greeting("taeil");
        mocked.greeting("earth");
        
        // 스트링만 저장하는 저장공간 생성
        var captor = ArgumentCaptor.forClass(String.class);
        verify(mocked, times(3)).greeting(captor.capture());

        var expected = List.of("world", "taeil", "earth");
        // assertLineseMatch를 사용해도 될듯.
        assertIterableEquals(expected, captor.getAllValues());
    }
}
````
forClass를 통해서 인자를 저장할 저장공간을 생성하고, capture를 통해서 greeting 메서드에 전달되는 인자들을 저장한다.  
getAllValues를 호출하여 저장했던 값에 접근하고 assertIterableEquals로 저장했던 값들과 기대하는 값들을 비교한다.  


# Mockito Spy
Mock 객체는 모든 메서드를 복사해서 기본적으로 기본값을 반환하고 아무것도 하지 않는다. 그런데, 특정 메서드는 정말로 동작해야 한다면? stubbing 하지 않고 호출되는지 여부를 알고싶은거라면 어떻게 해야할까?  
Spy 객체는 내부적으로 실제 Spy하려는 클래스의 인스턴스를 wrapping한다. stubbing되지 않았다면 wrapping한 인스턴스의 메서드를 실행하고 기록하며 stubbing했다면 더 이상 wrapping한 인스턴스의 메서드를 실행하지 않는다.    

## Spy 객체 생성
spy static 메서드를 통해서 생성 가능하고 인자를 넘기지 않거나 spy로 만들려는 클래스의 Class를 전달한다.  
reified를 가변인자로 넘기는것처럼 보이지만 제네릭을 통해서 mocking 하려는 클래스 혹은 인터페이스를 감지하기 위한 트릭이며 인자가 없거나 Class를 넘기는 경우 무조건 기본 생성자가 있어야한다.   
없다면 에러가 밣생하고, 기본 생성자를 강제로 만들필요 없이 객체를 전달하고 기존 객체를 wrapping할 수도 있다.  

````java
public static <T> T spy(T object) {
        return MOCKITO_CORE.mock(object.getClass(), withSettings().spiedInstance(object).defaultAnswer(CALLS_REAL_METHODS));
    }

    public static <T> T spy(Class<T> classToSpy) {
        return MOCKITO_CORE.mock(classToSpy, withSettings().useConstructor(new Object[0]).defaultAnswer(CALLS_REAL_METHODS));
    }

    @SafeVarargs
    public static <T> T spy(T... reified) {
        if (reified.length > 0) {
            throw new IllegalArgumentException("Please don't pass any values here. Java will detect class automagically.");
        } else {
            return spy(getClassOf(reified));
        }
    }
````
````java
public class CreateSpyExampleTest {
    static class Foo {
        Foo(String name) {}
        void bar() {}
    }

    @TestToFail
    void failToCreateSpy() {
        Foo a = spy(Foo.class);
        a.bar();
    }

    @Test
    void createSpy() {
        GreetingService spy = spy();
        assertNotNull(spy);
    }

    @Test
    void createSpyByObj() {
        GreetingService obj = new GreetingService();
        GreetingService spy = spy(obj);
        assertNotNull(spy);
    }
}
````
Foo 클래스는 기본 생성자가 없기 때문에 failToCreateSpy는 Excpetion을 throw 하게 되고 spy static 메서드를 호출하거나 객체를 생성해서 전달해야 한다.  

## Spy 객체 실행 
````java
@Test
    void test1() {
        GreetingService spy = spy();
        verify(spy, never()).greeting(anyString());

        var greeting = spy.greeting("world");
        assertEquals("hello world", greeting);
        verify(spy).greeting("world");

        when(spy.greeting("world"))
                .thenReturn("hoi world")
                .thenCallRealMethod()
                .thenThrow(ArithmeticException.class);

        greeting = spy.greeting("world");
        assertEquals("hoi world", greeting);
        verify(spy, times(2)).greeting("world");

        greeting = spy.greeting("world");
        assertEquals("hello world", greeting);
        verify(spy, times(3)).greeting("world");

        assertThrows(ArithmeticException.class, () -> {
            spy.greeting("world");
        });
    }
}
````
처음에는 Never 조건을 충족하고 stubbing을 하지 않고 호출한 경우, 기존 구현처럼 hello world를 반환한다.  
특정 값 반환, 실제 메서드 호출, exception throw를 체이닝해서 사용할 수 있다. times를 보면 stubbing을 했건 안했건 상관없이 호출 횟수는 누적된다.  
그리고 Spy 객체를 실행할때 Stubbing을 한 상태에서 실제 메서드를 호출하고 싶은 경우에는 thenCallRealMethod, doCallRealMethod, InvocationOnMock, callRealMethod를 통해서 **Spy 한정 실제 함수를 호출**할 수 있다.

# Mockito 어노테이션
각각의 테스트에서 매번 Mock, Spy, ArgumentCaptor를 생성하는것은 비효율적이다. 만약 여러 테스트에서 같이 사용한다면 BeforeEach에서 매번 초기화를 진행 해야한다.  
mockito에서는 이를 위해 어노테이션을 제공한다.  
**@Mock** : 어노테이션이 붙은 클래스를 기반으로 Mock 객체를 생성한다.  
**@Spy** : 어노테이션이 붙은 클래스를 기반으로 Spy 객체를 생성한다.  
**@Captor** : 어노테이션이 붙은 클래스를 기반으로 ArgumentCaptor 객체를 생성한다.  
**@InjectMocks** : Mock 객체를 주입하고 싶은 객체에 붙이면 타입을 기반으로 Mock 객체를 탐색하여 주입하고 @Spy와 같이 사용 가능하다. -> Autowired 와 비슷하다고 생각하면 되고, @MockitoAnnotations.openMocks를 어디선가 호출해야만 동작한다.  
````java
public class AnnotationExampleTest {
    @RequiredArgsConstructor
    private static class GreetingController {
        private final GreetingService greetingService;
    }

    @Spy
    @InjectMocks
    private GreetingController greetingController;

    @Mock
    private GreetingService greetingService;

    @Captor
    private ArgumentCaptor<String> captor;

    @BeforeEach
    void setUp() {
        // 테스트가 실행되기 전에 꼭 해줘야함
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void captureArgument() {
        greetingService.greeting("world");

        verify(greetingService).greeting(captor.capture());
        assertEquals("world", captor.getValue());
    }

    @Test
    void test1() {
        assertNotNull(greetingController.greetingService);

        assertTrue(MockUtil.isSpy(greetingController));
        assertTrue(MockUtil.isMock(
                greetingController.greetingService));
    }
}
````
````java
@ExtendWith(MockitoExtension.class)
public class AnnotationExtensionsExampleTest {
    @RequiredArgsConstructor
    static class GreetingController {
        private final GreetingService greetingService;
    }

    @Spy
    @InjectMocks
    private GreetingController greetingController;

    @Mock
    private GreetingService greetingService;

    @Captor
    private ArgumentCaptor<String> captor;

    @Test
    void captureArgument() {
        greetingService.greeting("world");

        verify(greetingService).greeting(captor.capture());
        assertEquals("world", captor.getValue());
    }

    @Test
    void test1() {
        assertNotNull(greetingController.greetingService);

        assertTrue(MockUtil.isSpy(greetingController));
        assertTrue(MockUtil.isMock(
                greetingController.greetingService));
    }
}
````
greetingService, captor 모두 별도로 생성하지 않고 자동으로 생성되었는지 체크하고, test1에서 MockUtil의 isSpy와 isMock을 통해 자동으로 생성된 객체들이 Spy와 Mock이 맞는지 검증한다.   
추가로, BeforeEach에서 MockitoAnnotations.openMocks(this); 를 매번 호출해야 한다고 했는데 MockitoExtension과 JUnit의 ExtendWith를 사용하면 MockitoAnnotations.openMocks를 직접 호출하지 않아도 MockitoExtension 내부적으로 호출하고 있기 때문에 자동으로 적용된다. 
