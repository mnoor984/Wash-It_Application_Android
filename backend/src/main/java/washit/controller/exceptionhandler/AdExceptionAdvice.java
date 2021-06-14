package washit.controller.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import washit.service.exception.AdException;

@ControllerAdvice
public class AdExceptionAdvice {
  @ResponseBody
  @ExceptionHandler(AdException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String adExceptionHandler(AdException e) {
    return e.getMessage();
  }
}
