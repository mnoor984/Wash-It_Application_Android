package washit.controller.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import washit.service.exception.AccountException;

@ControllerAdvice
public class AccountExceptionAdvice {

  @ResponseBody
  @ExceptionHandler(AccountException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String accountExceptionHandler(AccountException e) {
    return e.getMessage();
  }

}
