package wanderhub.server.global.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleCustomException(CustomLogicException e) {
        return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.valueOf(e.getExceptionCode().getStatus()));
    }
}
