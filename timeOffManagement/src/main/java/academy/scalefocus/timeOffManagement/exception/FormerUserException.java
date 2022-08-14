package academy.scalefocus.timeOffManagement.exception;

public class FormerUserException extends RuntimeException {

    private static final String FORMER_USER_TEMPLATE = "User with id %s is not part of the company.";

    public FormerUserException(Long id) {
        super(String.format(FORMER_USER_TEMPLATE, id));
    }

}
