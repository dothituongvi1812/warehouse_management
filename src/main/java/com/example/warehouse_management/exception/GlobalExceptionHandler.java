package com.example.warehouse_management.exception;

import com.example.warehouse_management.payload.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value={ NotFoundGlobalException.class })  // Có thể bắt nhiều loại exception
    public ResponseEntity<?> handleExceptionA(NotFoundGlobalException e) {
        ErrorResponse errorResponse =ErrorResponse.builder().statusCode(HttpStatus.NOT_FOUND.value()).message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse,new HttpHeaders(),HttpStatus.NOT_FOUND);


    }
    @ExceptionHandler(value={ ExistedException.class })
    public ResponseEntity<?> handleExceptionA(ExistedException e) {
        ErrorResponse errorResponse =ErrorResponse.builder().statusCode(HttpStatus.CONFLICT.value()).message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse,new HttpHeaders(),HttpStatus.CONFLICT);


    }
    @ExceptionHandler(value={ ErrorException.class })
    public ResponseEntity<?> handleExceptionA(ErrorException e) {
        ErrorResponse errorResponse =ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse,new HttpHeaders(),HttpStatus.BAD_REQUEST);


    }
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Map<String, String> body = new HashMap<>();
        ex.getBindingResult().getAllErrors().stream().forEach(error ->
        {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            body.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


}
