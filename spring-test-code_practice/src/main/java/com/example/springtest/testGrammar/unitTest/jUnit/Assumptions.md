## Assumptions
테스트 케이스를 계속 진행할지 결정하는 메서드를 제공하는 클래스로, 계속 진행하지 않는다면 해당 테스트는 무시한다.  
테스트가 실행될 수 있는 조건을 나타내며, 특정 환경 변수가 설정되어있는지, 특정 파일이 존재하는지 등..을 체크해서 사용할 수 있다.

## abort
실행시 무조건 해당 테스트 케이스를 무시하는 메서드
````java

import static org.junit.jupiter.api.Assumptions.*;

public class AbortTest {
    @TestToIgnore
    void test1() {
        var hasproblem = true;
        if (hasproblem) {
            abort();
        }
    }
}
````

## assumeTrue, assumeFalse
주어진 조건이 참인지 거짓인지 판별 후 무시를 결정하고, BooleanSupplier와 lambda를 인자로 전달 가능하다.
assumeTrue는 주어진 값이 true 이면 테스트 진행, assumeFalse 주어진 값이 false이면 테스트 진행한다.
````java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assumptions.*;

public class AssumeTrueFalseExampleTest {

    @Test
    void checkTrue() {
        assumeTrue(true);
    }

    @Test
    void checkTrueWithSupplier() {
        assumeTrue(() -> {
            return true;
        });
    }

    @Test
    void checkFalse() {
        assumeFalse(false);
    }

}
````

## assumingThat
인자로 전달받은 assumption이 true인 경우에만 Executable을 실행하여 Exception을 throw하는지 확인한다.  
assumption이 false라면 Executable을 실행하지 않고, Executable안에 assert 조건들을 추가하여 특정 조건을 충족하는 경우에만 테스트 수행 가능하다.  

````java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class AssumingThatExampleTest {
    @Test
    void test1() {
        var env = System.getenv("dev");
        if(env == null) {
            env = "local";
        }

        assumingThat(env.equals("alpha"), () -> {
           assertEquals(2,1);
        });
    }
}
````
dev 환경변수가 제공되지 않는다면, env 변수에 local이 추가하고, env.equals("alpha"")가 false 되어서 assertEquals가 실행되지 않았다. 자주 쓰이지는 않고 특정한 환경변수, 특정한 환경에서 테스트를 수행할때 많이 사용된다.  
