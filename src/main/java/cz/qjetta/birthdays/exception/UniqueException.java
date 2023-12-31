package cz.qjetta.birthdays.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UniqueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UniqueException(String uniqueDescription, String value) {
		super("Unique constraint for %s is %s".formatted(uniqueDescription,
				value));
	}
}
