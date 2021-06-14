package washit.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AccountException extends RuntimeException {
	public AccountException(String msg) {
		super(msg);
	}
}
