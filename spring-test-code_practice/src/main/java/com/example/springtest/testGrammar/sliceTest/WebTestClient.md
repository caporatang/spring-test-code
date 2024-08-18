## Slice Test - spring-webflux

# WebTestClient
spring-webflux에서 통신객체로 사용되는 webclient와 유사하며, webClient에서 제공되는 메서드를 동일하게 사용할 수 있고 WebTestClient는 별도의 생성 메서드와 body, cookie, header, status 검증 메서드를 제공한다.  

## WebTestClient 생성
webTestController를 생성할 수 있는 방법은 총 5가지가 있다.  
1. bindToController : controller들을 제공하여 WebTestClient를 생성한다. 
2. bindToRouterFunction : RouterFunction들을 제공하여 WebTestClient를 생성한다.
3. bindToApplicationContext : ApplicationContext 내의 bean들을 기반으로 WebTestClient를 생성한다.
4. bindToWebHandler : WebHandler를 직접 제공하여 WebTestClient를 생성한다.
5. bindToServer : reactor netty client를 사용해서 떠있는 서버에 직접 연결한다.
````java
static ControllerSpec bindToController(Object... controllers) {
        return new DefaultControllerSpec(controllers);
    }

    static RouterFunctionSpec bindToRouterFunction(RouterFunction<?> routerFunction) {
        return new DefaultRouterFunctionSpec(routerFunction);
    }

    static MockServerSpec<?> bindToApplicationContext(ApplicationContext applicationContext) {
        return new ApplicationContextSpec(applicationContext);
    }

    static MockServerSpec<?> bindToWebHandler(WebHandler webHandler) {
        return new DefaultMockServerSpec(webHandler);
    }

    static Builder bindToServer() {
        return new DefaultWebTestClientBuilder();
    }

    static Builder bindToServer(ClientHttpConnector connector) {
        return new DefaultWebTestClientBuilder(connector);
    }
````  
  
## MockServerSpec ?
bindToApplicationContext와 bindToWebHandler를 호출했을때 반환되는 MockServerSpec은 webFilter, webSessionManager, MockServerConfigurer등을 지원하며 configureClient를 통해 WebTestClient의 Builder를 반환하거나 build를 호출하여 바로 WebTestClient를 생성한다.  
````java
public interface MockServerSpec<B extends MockServerSpec<B>> {
        <T extends B> T webFilter(WebFilter... filter);

        <T extends B> T webSessionManager(WebSessionManager sessionManager);

        <T extends B> T apply(MockServerConfigurer configurer);

        Builder configureClient();

        WebTestClient build();
    }
````

## ControllerSpec ? 
bindToController를 사용해서 WebTestClient를 생성했을 때 반환되는 객체다.  
controllerAdvice, contentRtpeResolver, corsMapping, pathMatching, argumentResolver, httpMessageCodec, formatter, validator, viewResolver등을 설정할 수 있고 MockServerSpec을 상속하여 build를 호출하여 WebTestClient를 생성하거나 configureClient로 다시 Builder로 나갈 수 있다.   
````java
public interface ControllerSpec extends MockServerSpec<ControllerSpec> {
        ControllerSpec controllerAdvice(Object... controllerAdvice);

        ControllerSpec contentTypeResolver(Consumer<RequestedContentTypeResolverBuilder> consumer);

        ControllerSpec corsMappings(Consumer<CorsRegistry> consumer);

        ControllerSpec pathMatching(Consumer<PathMatchConfigurer> consumer);

        ControllerSpec argumentResolvers(Consumer<ArgumentResolverConfigurer> configurer);

        ControllerSpec httpMessageCodecs(Consumer<ServerCodecConfigurer> configurer);

        ControllerSpec formatters(Consumer<FormatterRegistry> consumer);

        ControllerSpec validator(Validator validator);

        ControllerSpec viewResolvers(Consumer<ViewResolverRegistry> consumer);
    }
````

## bindToController 사용 예제
SpringExtension, ContextConfiguration으로 GreetingController와 Advice를 bean으로 등록하고, 
WebTestClient의 bindToController와 ControllerSpec을 활용하여 WebTestClient를 생성한다.  
````java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                GreetingController.class,
                GreetingControllerAdvice.class
        }
)
public class CreateWebTestClientByControllerExampleTest {
        @MockBean
        GreetingService mockGreetingService;

        @Autowired
        GreetingController greetingController;

        @Autowired
        GreetingControllerAdvice greetingControllerAdvice;

        WebTestClient webTestClient;

        @BeforeEach
        void setup() {
                webTestClient = WebTestClient.bindToController(
                                greetingController
                        ).corsMappings(cors ->
                                cors.addMapping("/api/**"))
                        .controllerAdvice(greetingControllerAdvice)
                        .build();
        }

}
````

## bindToApplicationContext 사용 예제
bindToApplicationContext는 @EnableWebFlux를 사용해줘야한다.    
@EnableWebFlux를 사용하게 되면, DispatcherHanlder가 bean으로 등록되게 되고, DispatcherHandler가 등록이 되야 관련된 webFilter, exceptionHandler 등을 사용할 수 있기 때문이다.
````java
@EnableWebFlux
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                GreetingController.class,
                GreetingControllerAdvice.class
        }
)
public class CreateWebTestClientByApplicationContextExampleTest {
    @MockBean
    GreetingService mockGreetingService;

    @Autowired
    ApplicationContext applicationContext;

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToApplicationContext(
                applicationContext
        ).build();
    }
}
````

## bindToServer 사용 예제
기존에 떠있는 서버에 연결하여 WebTestClient를 생성하는 방식이다.  
bindToServer의 반환값은 builder이기 떄문에, builder를 활용하여 baseUrl을 설정해서 build한다.
````java
@ExtendWith(SpringExtension.class)
public class CreateWebTestClientByServerExampleTest {

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }
}
````

# WebTestClient 검증
WebTestClient는 WebClient의 exchangeToMono, exchangeToFlux, retrieve를 갖지 않는다. 대신 exchange()를 통해서 결과를 반환한다.
````java
public interface WebTestClient {
    /* another mothod .. */
    ResponseSpec exchange();    
}
````
exchange의 반환값은 ResponseSpec이며, ReponseSpec은 status, header, cookie, body에 대한 assertions들을 제공하고, return Result를 통해서 결과를 FluxExchangeResult 형태로 반환할 수 있다.  
````java
public interface ResponseSpec {
        ResponseSpec expectAll(ResponseSpecConsumer... consumers);

        StatusAssertions expectStatus();

        HeaderAssertions expectHeader();

        CookieAssertions expectCookie();

        <B> BodySpec<B, ?> expectBody(Class<B> bodyType);

        <B> BodySpec<B, ?> expectBody(ParameterizedTypeReference<B> bodyType);

        <E> ListBodySpec<E> expectBodyList(Class<E> elementType);

        <E> ListBodySpec<E> expectBodyList(ParameterizedTypeReference<E> elementType);

        BodyContentSpec expectBody();

        <T> FluxExchangeResult<T> returnResult(Class<T> elementClass);

        <T> FluxExchangeResult<T> returnResult(ParameterizedTypeReference<T> elementTypeRef);

        @FunctionalInterface
        public interface ResponseSpecConsumer extends Consumer<ResponseSpec> {
        }
    }
````

## WebTestClient status 검증 
StatusAssertions에서는 http 상태를 검증한다.  
httpStatus가 특정 값이랑 일치하는지 혹은 성공했는지를 검증하며, 기대와 다르다면 AssertionError throw하고 ResponseSpec을 반환하여 다른 Assertions를 체이닝할 수 있다.  
주요 메서드는 isOk, isCreated, isAccepted, is2xxSuccessful, is4xxClientError 등.. 이 있다.   
````java
public WebTestClient.ResponseSpec isEqualTo(int status) {
        return this.isEqualTo(HttpStatusCode.valueOf(status));
    }

    public WebTestClient.ResponseSpec isOk() {
        return this.assertStatusAndReturn(HttpStatus.OK);
    }

    public WebTestClient.ResponseSpec isCreated() {
        return this.assertStatusAndReturn(HttpStatus.CREATED);
    }

    public WebTestClient.ResponseSpec isAccepted() {
        return this.assertStatusAndReturn(HttpStatus.ACCEPTED);
    }

    public WebTestClient.ResponseSpec isNoContent() {
        return this.assertStatusAndReturn(HttpStatus.NO_CONTENT);
    }

    public WebTestClient.ResponseSpec isFound() {
        return this.assertStatusAndReturn(HttpStatus.FOUND);
    }

    public WebTestClient.ResponseSpec isSeeOther() {
        return this.assertStatusAndReturn(HttpStatus.SEE_OTHER);
    }

    public WebTestClient.ResponseSpec isNotModified() {
        return this.assertStatusAndReturn(HttpStatus.NOT_MODIFIED);
    }
````
WebTestClient로 요청을 보내고 응답값이 200, 2xx인지 확인하는 예제코드이다.
````java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                GreetingController.class,
        }
)
public class WebTestClientStatusExampleTest {
    @MockBean
    GreetingService mockGreetingService;

    @Autowired
    GreetingController greetingController;

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(
                greetingController
        ).build();
    }

    @Test
    void when_call_then_return_greeting() {
        // given
        String message = "msg";
        when(mockGreetingService.greetingMono(anyString()))
                .thenReturn(Mono.just(message));

        // when
        webTestClient.get()
                .uri("/greeting?who=taeil")
                .exchange() // exchange를 통해서 ResponseSpec을 반환받고,
                .expectStatus() // 상태가 isOk(200) 인지 확인하고
                .isOk()
                .expectStatus() // isOk가 반환한 ResponseSpec을 통해서 다시 
                .is2xxSuccessful(); // 2xx로 성공했는지를 체크
    }
}

````

## WebTestClient header 검증
HeaderAssertions에서는 http header를 검증하는 메서드를 제공한다.  
특정 headerName에 값을 포함하는지, 포함한다면 기대하는 값이랑 동일한지 확인하고 Content-*, Last-Modified, Cache-Control, Expires, Location등의 header를 바로 검증할 수 있는 편의 메서드도 제공한다.  
````java
public class HeaderAssertions {
    /* another method ... */
    public WebTestClient.ResponseSpec valueEquals(String headerName, String... values) {}
    public WebTestClient.ResponseSpec valueEquals(String headerName, long value) {}
    public WebTestClient.ResponseSpec valueEqualsDate(String headerName, long value) {}
    public WebTestClient.ResponseSpec valueMatches(String name, String pattern) {}
    public WebTestClient.ResponseSpec valuesMatch(String name, String... patterns) {}
    /* another method ... */ 
}
````
valueEquals가 가변인자인 이유는 spring web에서 하나의 헤더 이름으로 여러번 요청을 중복으로 보내는것이 가능하기 때문이다.  
헤더 검증 예제를 보면 새로운 Controller 메서드에서 header를 포함하여 응답을 반환하고 X-WHO, X-AGE에 대한 검증을 Header를 검증한다.
````java
@GetMapping("/header")
    public Mono<ResponseEntity<String>> greetingWithHeader(
        @RequestParam String who,
        @RequestParam Long age
    ) {
        return greetingService.greetingMono(who)
                .map(message -> ResponseEntity.ok()
                        .header("X-WHO", who)
                        .header("X-AGE", age.toString())
                        .body(message));
    }

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                GreetingController.class,
        }
)
public class WebTestClientHeaderExampleTest {
    @MockBean
    GreetingService mockGreetingService;

    @Autowired
    GreetingController greetingController;

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(
                greetingController
        ).build();
    }

    @Test
    void when_call_then_return_greeting() {
        // given
        String message = "msg";
        when(mockGreetingService.greetingMono(anyString()))
                .thenReturn(Mono.just(message));

        // when
        webTestClient.get()
                .uri("/greeting/header?who=taeil&age=29")
                .exchange()
                .expectHeader() // headerAssertions에 접근
                .contentType("text/plain;charset=UTF-8")//contentType체크
                .expectHeader()
                .exists("X-WHO")
                .expectHeader()
                .doesNotExist("X-EMAIL")
                .expectHeader()
                .value("X-WHO", header -> {
                    assertEquals("taeil", header);
                })
                .expectHeader()
                .valueEquals("X-AGE", 20L);
    }
}
````

## WebTestClient cookie 검증 
CookieAssertions 에서는 http cookie를 검증한다. Cookie의 값, maxAge, path, domain, secure, httpOnly, sameSite등을 검증할 수 있다.  
````java
public class CookieAssertions {

    /* another method .. */
    public WebTestClient.ReponseSpec valueEquals(String name, String value){}
    public WebTestClient.ReponseSpec value(String name, Consumer<String> consumer){}
    public WebTestClient.ReponseSpec exists(String name){}
    public WebTestClient.ReponseSpec doesNotExist(String name){}
    public WebTestClient.ReponseSpec maxAge(String name, Duration expected){}
    public WebTestClient.ReponseSpec path(String name, Duration expected){}
    public WebTestClient.ReponseSpec domain(String name, Duration expected){}
    /* another method .. */
}
````
````java
    @GetMapping("cookie")
    public Mono<String> greetingWithCookie(
            @RequestParam String who,
            ServerWebExchange exchange
    ){
        // 쿠키 생성
        var cookie = ResponseCookie.from("who", who) 
                .maxAge(3600)
                .domain("taeil.kim")
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
        // 만든 쿠키를 response에 세팅
        exchange.getResponse().addCookie(cookie);
        return greetingService.greetingMono(who);
    }

    
    
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                GreetingController.class,
        }
)
public class WebTestClientCookieExampleTest {
    @MockBean
    GreetingService mockGreetingService;

    @Autowired
    GreetingController greetingController;

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(
                greetingController
        ).build();
    }

    @Test
    void when_call_then_return_greeting() {
        // given
        String message = "msg";
        when(mockGreetingService.greetingMono(anyString()))
                .thenReturn(Mono.just(message));

        // when
        var cookieName = "who";
        
        // exchange를 통해서 반환받은 쿠키에 대한 값을 검증
        webTestClient.get()
                .uri("/greeting/cookie?who=grizz")
                .exchange()
                .expectCookie().exists(cookieName)
                .expectCookie().valueEquals(cookieName, "grizz")
                .expectCookie().domain(cookieName, "grizz.kim")
                .expectCookie().httpOnly(cookieName, true)
                .expectCookie().path(cookieName, "/")
                .expectCookie().secure(cookieName, true)
                .expectCookie()
                .maxAge(cookieName, Duration.ofSeconds(3600));
    }
}
    
````

## WebTestClient body 검증 
http 응답 body를 검증한다. expectBody, expectBodyList는 주어진 Class 혹은 ParameterizedTypeReference로 body를 decode 후 검증한다.  
만약 decode가 실패하면 검증에 실패하게 되고 expectBody는 BodyContentSpec을 반환한다.
바디가 어떤 타입으로 반환되는지, JSON으로 반환된다면 expectBody를 사용하고, 리스트 형태를 가진다면 expectBodyList를 사용하고, body 타입에 맞게 적절하게 사용하는게 중요하다.
````java
interface ResponseSpec {
    <B> BodySpec<B, ?> expectBody(Class<B> bodyType);

    <B> BodySpec<B, ?> expectBody(ParameterizedTypeReference<B> bodyType);
    
    <E> ListBodySpec<E> expectBodyList(Class<E> elementType);
    
    <E> ListBodySpec<E> expectBodyList(
            ParameterizedTypeReference<E> elementType);
    
    BodyContentSpec expectBody();
}
````
BodySpec은 isEqualTo, value 등으로 값을 비교하는 메서드를 제공한다.  
consumeWith를 통해서 Consumer를 제공하여 결과를 인자로 받고 Assertions를 사용할 수 있고, returnResult를 통해서 결과를 EntityExchangeResult로 전달한다. EntityExchangeResult의 getReponseBody를 통해서 body에 직접 접근이 가능하며 ListBodySpec은 body가 list로 제공되었다는 가정하에 검증할 수 있다.  

````java
public interface BodySpec<B, S extends BodySpec<B, S>> {
        <T extends S> T isEqualTo(B expected);

        <T extends S> T value(Matcher<? super B> matcher);

        <T extends S, R> T value(Function<B, R> bodyMapper, Matcher<? super R> matcher);

        <T extends S> T value(Consumer<B> consumer);

        <T extends S> T consumeWith(Consumer<EntityExchangeResult<B>> consumer);

        EntityExchangeResult<B> returnResult();
    }
````

BodyContentSpec은 body가 decode되지 않은 상황에서 검증할 수 있는 메서드를 제공한다.  
consumeWith의 경우, body를 byte[] 형태로 제공하며, returnResult를 통해서 body를 byte[] 형태로 직접 접근할 수도 있다.
````java
public interface BodyContentSpec {
        EntityExchangeResult<Void> isEmpty();

        default BodyContentSpec json(String expectedJson) {
            return this.json(expectedJson, false);
        }

        BodyContentSpec json(String expectedJson, boolean strict);

        BodyContentSpec xml(String expectedXml);

        JsonPathAssertions jsonPath(String expression, Object... args);

        default XpathAssertions xpath(String expression, Object... args) {
            return this.xpath(expression, (Map)null, args);
        }

        XpathAssertions xpath(String expression, @Nullable Map<String, String> namespaces, Object... args);

        BodyContentSpec consumeWith(Consumer<EntityExchangeResult<byte[]>> consumer);

        EntityExchangeResult<byte[]> returnResult();
    }
````
BodyContentSpec은 BodySpec보다 훨씬 열려있어서 원하는 타입별로 테스트를 할 수 있는 메서드들을 제공하는 인터페이스라고 생각하면 된다.    
예제 코드를 살펴보면 expectBody를 통해서 body를 GreetingResponse 형태로 decode하고 isEqualTo를 통해서 동일한 객체인지를 비교한다. 추가로 value와 Consumer를 사용해서 더 복잡한 조건을 검증한다.  
GreetingResponse 객체를 추가로 만든 이유는 동일한 객체인지 체크하기 위함이다.

````java
@GetMapping("/body")
public Mono<GreetingResponse> greetingWithBody(
        @RequestParam String who,
        @RequestParam Long age
    ) {
        return greetingService.greetingMono(who)
        .map(message -> new GreetingResponse(message, age, who));
    }


@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                GreetingController.class,
        }
)
public class WebTestClientBodyExampleTest {
    @MockBean
    GreetingService mockGreetingService;

    @Autowired
    GreetingController greetingController;

    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(
                greetingController
        ).build();
    }

    @EqualsAndHashCode
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class GreetingResponse {
        private String message;
        private String who;
        private int age;
    }

    @Test
    void when_call_then_return_greeting() {
        // given
        String message = "msg";
        when(mockGreetingService.greetingMono(anyString()))
                .thenReturn(Mono.just(message));

        // when
        var expected = new GreetingResponse(message, "taeil", 29);

        webTestClient.get()
                .uri("/greeting/body?who=taeil&age=29")
                .exchange()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(GreetingResponse.class)
                .isEqualTo(expected)
                .value(greetingResponse -> {
                    assertTrue(greetingResponse.age > 0);
                });
    }
}
````

