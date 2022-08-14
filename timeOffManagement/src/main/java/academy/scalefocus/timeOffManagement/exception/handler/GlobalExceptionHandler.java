package academy.scalefocus.timeOffManagement.exception.handler;

import academy.scalefocus.timeOffManagement.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDetails> getError(final Exception exception, final HttpStatus httpStatus,
                                                  HttpServletRequest request) {

        return new ResponseEntity<>(new ErrorDetails(exception, request.getRequestURI()), httpStatus);
    }

    private ResponseEntity<ValidationErrorDetails> getValidationErrors(final HttpStatus httpStatus,
                                                                       Map<String, String> messages, HttpServletRequest request) {

        return new ResponseEntity<>(new ValidationErrorDetails(request.getRequestURI(), messages), httpStatus);
    }

    @ExceptionHandler(value = LoginException.class)
    public ResponseEntity<ErrorDetails> handleLoginException(HttpServletRequest request, LoginException e) {
        return getError(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(HttpServletRequest request,
                                                                       IllegalArgumentException e) {
        return getError(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserNotFountException(HttpServletRequest request,
                                                                    UserNotFoundException e) {

        return getError(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = TeamNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleTeamNotFountException(HttpServletRequest request,
                                                                    TeamNotFoundException e) {

        return getError(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = TeamHasNoMembersException.class)
    public ResponseEntity<ErrorDetails> handleTeamHasNoMembersException(HttpServletRequest request,
                                                                        TeamHasNoMembersException e) {

        return getError(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = TimeOffNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleTimeOffNotFountException(HttpServletRequest request,
                                                                       TimeOffNotFoundException e) {

        return getError(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = TimeOffDateException.class)
    public ResponseEntity<ErrorDetails> handleTimeOffDatesException(HttpServletRequest request,
                                                                    TimeOffDateException e) {

        return getError(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = TimeOffLimitExceededException.class)
    public ResponseEntity<ErrorDetails> handleTimeOffLimitExceededException(HttpServletRequest request,
                                                                            TimeOffLimitExceededException e) {

        return getError(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = TimeOffAlreadySubmittedException.class)
    public ResponseEntity<ErrorDetails> handleTimeOffAlreadySubmittedException(HttpServletRequest request,
                                                                               TimeOffAlreadySubmittedException e) {

        return getError(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ApproverNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleApproverNotFoundException(HttpServletRequest request,
                                                                        ApproverNotFoundException e) {

        return getError(e, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDetails> handleValidationExceptions(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return getValidationErrors(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler(value = FormerUserException.class)
    public ResponseEntity<ErrorDetails> handleFormerUserException(HttpServletRequest request, FormerUserException e) {
        return getError(e, HttpStatus.LOCKED, request);
    }
}
