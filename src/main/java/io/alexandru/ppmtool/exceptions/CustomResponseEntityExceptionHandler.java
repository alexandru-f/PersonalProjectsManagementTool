package io.alexandru.ppmtool.exceptions;

import org.hibernate.cfg.ExtendsQueueEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ProjectIdException.class)
	public final ResponseEntity<Object> projectIdExceptionHandler(ProjectIdException ex, WebRequest request){
		ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ProjectNotFoundException.class)
	public final ResponseEntity<Object> projectIdExceptionHandler(ProjectNotFoundException ex, WebRequest request){
		ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public final ResponseEntity<Object> exceptionHandler(UsernameAlreadyExistsException ex, WebRequest request){
		UsernameAlreadyExistsResponse exceptionResponse = new UsernameAlreadyExistsResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(PasswordsDoNotMatchException.class)
	public final ResponseEntity<Object> passwordsDoNotMatchExceptionHandler(PasswordsDoNotMatchException ex, WebRequest request) {
		PasswordsDoNotMatchResponse exceptionResponse = new PasswordsDoNotMatchResponse((ex.getMessage()));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}
	@ExceptionHandler(TokenNotFoundException.class)
	public final ResponseEntity<Object> tokenNotFoundExceptionHandler(TokenNotFoundException ex, WebRequest request) {
		TokenNotFoundResponse exceptionResponse = new TokenNotFoundResponse((ex.getMessage()));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}
	@ExceptionHandler(TokenExpiredException.class)
	public final ResponseEntity<Object> tokenExpiredExceptionHandler(TokenExpiredException ex, WebRequest request) {
		TokenExpiredResponse exceptionResponse = new TokenExpiredResponse((ex.getMessage()));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}
	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<Object> usernameNotFoundExceptionHandler(UsernameNotFoundException ex, WebRequest request) {
		UsernameNotFoundExceptionResponse exceptionResponse = new UsernameNotFoundExceptionResponse((ex.getMessage()));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}
}