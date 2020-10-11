package pl.lodz.p.it.auctionsystem.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.auctionsystem.exceptions.EntityNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<?> exception(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
