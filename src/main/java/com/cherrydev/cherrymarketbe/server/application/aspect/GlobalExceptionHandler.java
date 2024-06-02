package com.cherrydev.cherrymarketbe.server.application.aspect;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.cherrydev.cherrymarketbe.server.application.exception.ApplicationException;
import com.cherrydev.cherrymarketbe.server.application.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.ErrorResponse;

import static org.springframework.http.HttpStatus.*;


/**
 * Exception 처리를 위한 Advice
 * <p>
 * 자세한 로그는 서버 로그로만 기록하고, 클라이언트에게는 상태 코드와 간단한 메세지만 전달하도록 구성한다.
 * <p>
 * <i>요청, 예외에 대한 로깅은 AOP로 처리, 이곳에서는 예외 처리만 구성</i>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_MESSAGE_500 = "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.";

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex) {
        return ResponseEntity.
                status(INTERNAL_SERVER_ERROR).
                body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), ERROR_MESSAGE_500 + " : " + ex.getLocalizedMessage()));
    }

    @ExceptionHandler({ApplicationException.class})
    protected ResponseEntity<ErrorResponse> applicationException(ApplicationException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponse(ex.getStatusCode(), ex.getMessage()));
    }

    @ExceptionHandler({InsufficientStockException.class})
    protected ResponseEntity<ErrorResponse> handleInsufficientStockExceptionInternal(InsufficientStockException ex) {
        String message = ex.getMessage() + (ex.getGoodsName() != null ? " : " + ex.getGoodsName() : "");
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponse(ex.getStatusCode(), message));
    }

    /**
     * Parameter Validation 실패 시
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.
                status(BAD_REQUEST).
                body(new ErrorResponse(BAD_REQUEST.value(), generateValidationErrorDetails(ex.getBindingResult())));
    }

    private String generateValidationErrorDetails(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(fieldError -> """
                        %s - %s.
                        """.formatted(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining());
    }

    @ExceptionHandler({NoResourceFoundException.class})
    protected ResponseEntity<ErrorResponse> noResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.
                status(BAD_REQUEST).
                body(new ErrorResponse(BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    protected ResponseEntity<ErrorResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String message = "지원하지 않는 메서드입니다. Expected: " + ex.getSupportedHttpMethods() + " Actual : " + ex.getMethod();
        return ResponseEntity.
                status(METHOD_NOT_ALLOWED).
                body(new ErrorResponse(METHOD_NOT_ALLOWED.value(), message));
    }

}
