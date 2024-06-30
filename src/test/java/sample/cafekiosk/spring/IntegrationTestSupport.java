package sample.cafekiosk.spring;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.client.mail.MailSendClient;

@ActiveProfiles("test") // application.yml에 지정되어있는 on-profile중 어떤 profile로 실행할 것인지 선택하는 어노테이션
@SpringBootTest
public abstract class IntegrationTestSupport { // 테스트 환경을 통합하기 위한 추상 클래스

    @MockBean
    protected MailSendClient mailSendClient;
}
