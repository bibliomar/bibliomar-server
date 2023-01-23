package bibliomar.bibliomarserver.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(HttpServletRequest request,
                                                           ResponseStatusException ex) {
        Map<String, Object> errorsResponseMap = new HashMap<>();
        errorsResponseMap.put("status", ex.getStatusCode());
        errorsResponseMap.put("errors", ex.getReason());
        errorsResponseMap.put("message", ex.getMessage());
        errorsResponseMap.put("path", request.getServletPath());
        errorsResponseMap.put("time", Instant.now());
        return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(errorsResponseMap);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpServletRequest request,
                                                                   HttpMessageNotReadableException ex) {
        Map<String, Object> errorsResponseMap = new HashMap<>();
        errorsResponseMap.put("status", HttpStatus.BAD_REQUEST);
        errorsResponseMap.put("errors", ex.getMessage());
        errorsResponseMap.put("message", ex.getMessage());
        errorsResponseMap.put("path", request.getServletPath());
        errorsResponseMap.put("time", Instant.now());
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(errorsResponseMap);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                   MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        Map<String, Object> errorsResponseMap = new HashMap<>();
        errorsResponseMap.put("status", HttpStatus.BAD_REQUEST);
        errorsResponseMap.put("errors", errors);
        errorsResponseMap.put("message", ex.getMessage());
        errorsResponseMap.put("path", request.getServletPath());
        errorsResponseMap.put("time", Instant.now());

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(errorsResponseMap);
    }

}
