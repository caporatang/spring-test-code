## Slice Test
Controller는 http 요청을 받아서 처리하는 레이어에 해당하며 프레젠테이션 레이어라 하고,   
Repository는 데이터베이스에 연결하여 데이터의 조회, 저장, 수정, 삭제 등의 CRUD를 수행하는 데이터 엑세스 레이어에 해당한다.
````java
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    UserR2dbcRepository userR2dbcRepository;

    @Test
    void when_find_by_id_returns_user_mono() {
        // given
        var user = new UserEntity(1L, "taeil", 20, "1", "1234");

        System.out.println(user);

        var userR2dbcRepository = mock(UserR2dbcRepository.class, Mockito.RETURNS_DEEP_STUBS);

        when(userR2dbcRepository.findByName(any()))
                .thenReturn(Mono.just(user));
        // when
        var resp = userR2dbcRepository.findByName("taeil");

        //then
        StepVerifier.create(resp)
                .assertNext(u -> {
                    System.out.println("test!");
                    assertEquals("taeil", u.getName());
                    assertEquals(20, u.getAge());
                })
                .verifyComplete();
    }
}
````
예시 코드처럼 http 혹은 데이터베이스 부분을 mocking 해서 테스트를 작성하는것은 의미가 없다. 왜냐하면 R2dbcRepository는 R2dbc를 사용했기 때문에 이미 검증된 DB를 가져다가 쓰는것이기 때문이다.  
이럴때 사용할 수 있는 기법이 **슬라이스 테스트** 이다.

스프링 프레임워크에서 제공하는 테스트 기능중에 하나이며, 하나의 레이어를 나눠서 테스트 한다는 이유에서 슬라이스 테스트라고 한다.  
슬라이스 테스트는 2가지 레이어로 나눌수있는데, 예를들면  
프레젠테이션 레이어 : @WebFluxTest  
데이터 엑세스 레이어 : @DataR2dbcTest, @DataRedisTest, @DataMongoTest 가 있다.  

# ContextConfiguration
테스트 클래스에 적용하여, 어떻게 ApplicationContext를 불러올지 설정할 수 있는 어노테이션이다.  
locations를 통해 xml 기반의 bean을 load하고, classes를 통해 어노테이션 기반의 bean을 load한다.
````java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ContextConfiguration {
    @AliasFor("locations")
    String[] value() default {};

    @AliasFor("value")
    String[] locations() default {};

    Class<?>[] classes() default {};

    Class<? extends ApplicationContextInitializer<?>>[] initializers() default {};

    boolean inheritLocations() default true;

    boolean inheritInitializers() default true;

    Class<? extends ContextLoader> loader() default ContextLoader.class;

    String name() default "";
}
````  
## @SpringBootApplication import
@SpringBootApplication을 import하여 ComponentScan을 활용할 수 있고, 이 경우 모든 bean들을 scan하려고 하기 때문에 불필요한 bean들 까지도 등록될 수 있다.  
````java
@ExtendWith({SpringExtension.class})
@ContextConfiguration(
        classes = SpringTestApplication.class
)
public class ContextConfigurationWithApplicationExampleTest {

    @Autowired
    GreetingController greetingController;

    @Test
    void when_request_get_then_return_greeting() {
        // given
        var who = "world";

        //when
        var result = greetingController.greeting(who);

        //then
        var expected = "hello world";
        StepVerifier.create(result)
                .expectNext(expected)
                .verifyComplete();

    }
}
````
@ContextConfiguration은 단독으로는 사용이 불가능하고 SpringExtension을 함께 사용해야한다. SpringExtension을 사용하여 스프링을 위한 테스트 환경을 구성하고 @SpringBootApplication을 import하여 ComponentScan을 활용한다.    

## @TestConfiguration 
@TestConfiguration을 이용해서 직접 bean을 등록하고, ContextConfiguration으로 ContextConfiguration으로 만든 TestConfiguration을 import할 수 있다.  
````java
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


@ExtendWith({SpringExtension.class})
@ContextConfiguration(
        classes = ExampleTestConfig.class
)
public class ContextConfigurationWithTestCOnfigurationExampleTest {

    @Autowired
    GreetingController greetingController;

    @Test
    void when_request_get_then_return_greeting() {
        // given
        var who = "world";

        //when
        var result = greetingController.greeting(who);

        //then
        var expected = "hello world";
        StepVerifier.create(result)
                .expectNext(expected)
                .verifyComplete();

    }
}
````
SpringTestApplication 을 전체를 가져와서 환경을 구성하는것보다 필요한 객체들만 TestConfiguration으로 구성해서 테스트하면 훨씬 수월하게 테스트할 수 있다.  

````java
@ExtendWith({SpringExtension.class})
@ContextConfiguration(
        classes = {
                GreetingController.class,
                GreetingService.class
        }
)
public class ContextConfigurationExampleTest {

    @Autowired
    GreetingController greetingController;

    @Test
    void when_request_get_then_return_greeting() {
        // given
        var who = "world";

        //when
        var result = greetingController.greeting(who);

        //then
        var expected = "hello world";
        StepVerifier.create(result)
                .expectNext(expected)
                .verifyComplete();

    }
}
````
클래스만 따로 임포트해서 테스트 하는것도 가능하다. mocking을 하지 않고 실제 클래스들을 사용하여 테스트하는 방법이다.  
실제 객체들을 가져다가 테스트하는것이기 때문에, bean으로 관리되고 있는 객체를 사용해야한다.  

# MockBean
@Bean처럼 bean을 등록할 수 있게 지원하며, @Bean과 다르게 실제 객체 대신 mock 객체를 생성하여 등록한다.

````java
@ExtendWith({SpringExtension.class})
@ContextConfiguration(
        classes = {
                GreetingController.class,
        }
)
public class ContextConfigurationFailedExampleTest {
    @Autowired
    GreetingController greetingController;

    @Test
    void when_request_get_then_return_greeting() {
        // given
        var who = "world";

        // when
        var result = greetingController.greeting(who);

        //then
        var expected = "hello world";
        StepVerifier.create(result)
                .expectNext(expected)
                .verifyComplete();
    }
}

@ExtendWith({SpringExtension.class})
@ContextConfiguration(
        classes = {
                GreetingController.class,
        }
)


public class ContextConfigurationMockBeanExampleTest {
    @Autowired
    GreetingController greetingController;

    @MockBean
    GreetingService mockGreetingService;

    @Test
    void when_request_get_then_return_greeting() {
        // given
        var who = "world";
        var message = "msg";

        when(mockGreetingService.greetingMono(anyString()))
                .thenReturn(Mono.just(message));

        // when
        var result = greetingController.greeting(who);

        // then
        StepVerifier.create(result)
                .expectNext(message)
                .verifyComplete();
    }
}


````
ContextConfigurationFailedExampleTest 예제 같은 경우 GreetingController는 GreetingService bean을 의존으로 갖기 때문에 bean으로 등록되지 않은 경우, NoSuchBeanDefinitionException이 발생된다.  

ContextConfigurationMockBeanExampleTest의 경우 service를 MockBean으로 만들었다. GreetingController는 GreetingService의 의존성이 필요한데, SpringExtension이 MockBean을 Controller에 주입해준다.  
추가적으로 Service를 MockBean으로 만들고 stubbing해서 사용한다고 하더라도 SpringExtension에 의해 사용될때마다 초기화되기 떄문에, 다른 테스트에서 사용하는것은 문제가 없다.  

