package routine.log.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import routine.log.exception.routine.RoutineNotFoundException;
import routine.log.exception.user.UserNotFoundException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoutineNotFoundException.class)
    public ResponseEntity<?> handlerNotFound(RoutineNotFoundException e) {
        log.warn("NotFound: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {
        log.warn("Validation failed: {}", e.getMessage());
        return ResponseEntity.badRequest().build();
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException e) {
        log.warn("{}",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
