package academy.scalefocus.timeOffManagement.exception;

public class TimeOffNotFoundException extends RuntimeException {
    private static final String TIME_OFF_NOT_FOUND_TEMPLATE = "Time off with id %s not found";
    public TimeOffNotFoundException(Long id) {
        super(String.format(TIME_OFF_NOT_FOUND_TEMPLATE, id));
    }
}
