package academy.scalefocus.timeOffManagement.exception;

public class LoginException extends RuntimeException {

    private static final String LOGIN_EXCEPTION_TEMPLATE = "Invalid %s";

    public LoginException(String message) {

        super(String.format(LOGIN_EXCEPTION_TEMPLATE, message));
    }
}
