package academy.scalefocus.timeOffManagement.exception;

public class TeamHasNoMembersException extends RuntimeException {

    private static final String TEAM_HAS_NO_MEMBERS_TEMPLATE = "There are no members in team with id: %s";

    public TeamHasNoMembersException(Long id) {
        super(String.format(TEAM_HAS_NO_MEMBERS_TEMPLATE, id));
    }
}
