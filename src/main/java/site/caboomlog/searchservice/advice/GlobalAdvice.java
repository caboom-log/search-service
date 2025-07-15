package site.caboomlog.searchservice.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.caboomlog.searchservice.dto.ApiResponse;

@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder builder = new StringBuilder();
        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> builder.append(error.getDefaultMessage()).append("\n"));

        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest(builder.toString()));
    }
}
