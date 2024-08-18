## 테스트 조건부 실행
테스트 해야하는 클래스가 소수라면 Assumptions에서 제공하는 메서드들을 사용해서 테스트 실행을 막을 수 있겠지만, 규모가 커지거나 테스트할게 많아진다면 코드 가독성도 떨어지게 된다.  
JUnit에서는 상황에 따라 테스트를 조건에 맞게 실행할 수 있는 어노테이션들을 제공해준다.  

## @Disabled
Disabled 어노테이션이 붙은 테스트는 무조건 실행 계획에서 제외한다. 이는 Assumptions와 동일하게 무시되며, 메서드가 아니라 클래스 레벨에도 적용이 가능하다.  
@Disabled를 사용할때 value라는 메서드를 통해서 이유를 명시할 수 있는데 JUnit 개발팀에서는 간단하게라도 이유를 추가하는 것을 권장하고 있다.  

````java
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisabledTest {

    @Test
    @Disabled
    void test1() {
        assertEquals(1,1);
    }

    @Test
    @Disabled("~~ 버그 픽스 때까지 비활성화")
    void test2() {
        assertEquals(2,2);
    }
}
````

## @DisabledOn*, @EnabledOn*
@Disabled를 사용해서 비활성화 시켜도 되지만, 많은 클래스나 메서드를 다시 원복 시킬때 비용이 많이 들어가게 된다.  
그럴때 특정 환경이나 다양한 조건을 이용해서 테스트를 실행할지 혹은 무시할지 결정할 수 있는 메서드다. OS, Java버전, 버전 범위, 시스템 프로퍼티, 호나경 변수, 커스텀 조건 등..이 있고  
DisabledOn* 어노테이션이 붙었다면 조건을 충족하면 무시, 충족하지 않는다면 실행하고 EnabledOn* 어노테이션이라면 조건을 충족하면 실행, 충족하지 않는다면 무시한다.  


````java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EnabledOnOs(OS.MAC)
@Test
public @interface TestOnMac {
}


// --------------------------------------
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestOnMacExampleTest {

    @TestOnMac
    void test1() {
        assertTrue(true);
    }
}

````
대부분의 조건부 실행 어노테이션은 메타 어노테이션으로도 사용이 가능하다. 어노테이션을 생성해서 특정 환경에서만 동작하게끔 만들어줄수도있다.

## @Disabled(Enabled)OnOS
OS에 따라서 테스트 실행, 무시 여부를 결정한다. AIX, FREEBSD, LINUX, MAC,OPENBSD, SOLARIS, WINDOWS,OTHER 을 제공하며, architectures에는 OS 아키텍쳐를 적용 가능하다.  
OS 아키텍쳐는 System.getProperty("os.arch") 를 통해서 반환된 값과 비교하게 되는데 값은 arch64, x86_64, x86_32, amd64, x86, arm등 으로 비교한다.  
@Disabled와 마찬가지고 테스트를 무시하는 경우 이유를 명시할 수 있다.  
````java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Slf4j
public class DisabledOnOsExampleTest {

    @DisabledOnOs(OS.WINDOWS)
   @Test
   void testNotOnWindows() {
      log.info("testNotOnWindows");
   }

   @DisabledOnOs(OS.MAC)
    @Test
    void testNotOnMac() {
        log.info("testNotOnMac");
   }

   @DisabledOnOs(value = {OS.MAC}, architectures = {"x86_64"})
   @Test
   void testNotOnMacX86() {
        // 인텔 맥 getProperty 값
        log.info("testNotOnMacX86");
   }

    @DisabledOnOs(value = {OS.MAC}, architectures = {"aarch64"})
    @Test
    void testNotOnMacArch64() {
        log.info("testNotOnMacAarch64");
    }
}
````
각 사용되는 OS에 따라 실행 여부가 결정된다. m1 mac 을 사용중이기 때문에 testNotOnWindows, testNotOnMacX86 테스트만 실행된다.  @Enabled는 반대로 조건을 만족하는 메서드만 실행된다.  

## Disabled(Enabled)OnJre, @*ForJreRange
**Disabled(Enabled)OnJre**  
Java 버전에 따라서 테스트를 실행, 무시 여부를 결정한다. 여러 개의 Java 버전을 제공할 수 있고, 테스트를 무시하는 경우 이유를 제공할 수 있다.  
제공되는 enum 버전은 8 ~ 21 까지 제공되며, OTHER도 제공되며, 이유를 명시할 수 있다.  


**@*ForJreRange**  
테스트를 진행 혹은 무시할 Java 버전을 명시할 수 있고, min의 기본값은 JAVA_8, max의 기본값은 OTHER로 설정된다.
DisabledForJreRange는 min과 max 사이 기준에 속한다면 테스트를 무시, EnabledForJreRange의 경우 min max인 경우에만 테스트를 실행한다.  

````java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

@Slf4j
public class DisabledJreExampleTest {

    @DisabledOnJre(JRE.JAVA_11)
    @Test
    void testNotOnJava11() {
        log.info("testNotOnJava8");
    }

    @DisabledOnJre(value = {JRE.JAVA_11, JRE.JAVA_12})
    @Test
    void testNotOnJAva11And12() {
        log.info("testNotOnJAva11And12");
    }

    @DisabledOnJre(JRE.JAVA_10)
    @Test
    void testNotOnJava10() {
        log.info("testNotOnJava10");
    }
}
````
````java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

@Slf4j
public class DisabledForJreRangeExampleTest {

    @DisabledForJreRange(min = JRE.JAVA_8,
                         max = JRE.JAVA_11 )
    @Test
    void testNotOnJAva8To11() {
        log.info("testNotOnJAva8To11");
    }

    @DisabledForJreRange(min = JRE.JAVA_12)
    @Test
    void testNotOnJava12OrHigher() {
        log.info("testNotOnJava12OrHigher");
    }

    @DisabledForJreRange(max = JRE.JAVA_10)
    @Test
    void testNotOnJAva10OrLower() {
        log.info("testNotOnJava10OrLower");
    }
}
````

## @*IfSystemProperty
시스템 프로퍼티가 특정 정규표현식을 만족하는지 여부에 따라 테스트 실행/무시를 결정한다. named를 통해 시스템 프로퍼티 이름을 지정하고 matches를 통해 비교하는 대상이 되는 정규표현식을 작성한다.  
````java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

@Slf4j
public class DisabledIfSystemPropertyExampleTest {

    @DisabledIfSystemProperty(
            named = "test.bool",
            matches = "true"
    )
    @Test
    void testIfSystemPropertyIsNotTrue() {
        log.info("testIfSystemPropertyIsNotTrue");
    }

    @DisabledIfSystemProperty(
            named = "os.arch",
            matches = "[a-z1-9]+"
    )
    @Test
    void testIfSystemPropertyNotMatchesRegex() {
        log.info("testIfSystemPropertyNotMatchesRegex");
    }
}
````
os.arch 프로퍼티에 'aarch64'값이 할당되고, test.bool은 없기 때문에 testIfSystemPropertyIsNotTrue만 실행된다.   
그리고 @Disabled(Enabled)IfEnvironmentVariable을 통해 환경변수가 특정 정규표현식을 만족하는지 여부에 따라 테스트를 실행/무시 할 수 있다.  

## @DisabledIf, @EnabledIf
value에 메서드 이름을 전달하고 해당 메서드의 결과를 이용해서 테스트를 실행/무시할지 결정하는 메서드다.  
````java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.EnabledIf;

@Slf4j
public class DisabledIfEnabledIfExampleTest {

    boolean condition() {
        return true;
    }

    @DisabledIf(value = "condition")
    @Test
    void testIfConditionIsNotTrue() {
      log.info("testIfConditionIsNotTrue");
    }

    @EnabledIf(value = "condition")
    @Test
    void testIfConditionIsTrue() {
        log.info("testIfConditionIsTrue");
    }

}
````
