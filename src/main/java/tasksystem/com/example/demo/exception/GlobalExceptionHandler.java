package tasksystem.com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?>handleValidationExceptions(MethodArgumentNotValidException ex){
        String errorMessage=ex.getBindingResult()
                            .getFieldError()
                            .getDefaultMessage();
        Map<String,String>response=new HashMap<>();
        response.put("message", errorMessage);

        return ResponseEntity.badRequest().body(response);
    }
    
}
