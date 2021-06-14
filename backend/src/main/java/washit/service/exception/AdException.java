package washit.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AdException extends RuntimeException {
	public AdException(String msg) {
		super(msg);
	}
}