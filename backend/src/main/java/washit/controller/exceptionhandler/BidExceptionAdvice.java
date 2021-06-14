package washit.controller.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import washit.service.exception.BidException;

@ControllerAdvice
public class BidExceptionAdvice {
	  @ResponseBody
		@ExceptionHandler(BidException.class)
		@ResponseStatus(HttpStatus.BAD_REQUEST)
		String bidExceptionHandler(BidException e) {
			return e.getMessage();
		}

}
