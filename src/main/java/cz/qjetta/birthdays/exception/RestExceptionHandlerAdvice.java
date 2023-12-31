package cz.qjetta.birthdays.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandlerAdvice {
	@ExceptionHandler(RecordNotFoundException.class)
	ProblemDetail handleNotFoundException(RecordNotFoundException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
				e.getMessage());
	}

	@ExceptionHandler(UniqueException.class)
	ProblemDetail handleNotFoundException(UniqueException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
				e.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	ProblemDetail handleNotFoundException(ConstraintViolationException e) {
		String message = e.getConstraintViolations().stream().map(
				c -> "%s: %s".formatted(c.getPropertyPath(), c.getMessage()))
				.collect(Collectors.joining(". "));
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				message);
	}
}