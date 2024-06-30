package sample.cafekiosk.spring.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice   // 해당 클래스가 컨트롤러에서 발생하는 예외를 잡아서 처리하는 클래스임을 명시
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 해당 메서드가 실행되면 응답 코드를 400으로 설정
    @ExceptionHandler(BindException.class) // BindException 예외가 발생하면 해당 메서드를 실행
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), // 유효성 검사 실패 시 메시지, request의 valid 어노테이션에 설정한 메시지가 반환됨
                null
        );
    }
}
