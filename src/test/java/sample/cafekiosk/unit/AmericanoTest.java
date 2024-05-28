package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @Test
    void getName() {
        Americano americano = new Americano();

        // 테스트 코드로 1번 arg와 2번 arg가 같다는 뜻이다. junit의 코드이다.
        assertEquals(americano.getName(), "아메리카노");

        /*  테스크 코드로 americano.getName()과 아메리카노가 같다는 뜻이다.
            aeertJ의 코드이다. 체인 형태를 쓸 수 있어서 필요한 테스트과정을 계속 덧붙일 수 있어서
            편리하다는 장점이 있다.
         */
        assertThat(americano.getName()).isEqualTo("아메리카노");
    }

    @Test
    void getPrice() {
        Americano americano = new Americano();

        assertThat(americano.getPrice()).isEqualTo(4000);
    }

}
