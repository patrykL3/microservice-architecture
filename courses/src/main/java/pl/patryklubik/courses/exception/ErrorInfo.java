package pl.patryklubik.courses.exception;

/**
 * Create by Patryk Łubik on 25.08.2021.
 */
public enum ErrorInfo {
    COURSE_DATES_INCORRECT("Start date should not be after end date"),
    PARTICIPANTS_NUMBER_IS_NOT_FULL("Participants number shows that course is not FULL"),
    PARTICIPANTS_NUMBER_EXCEED_LIMIT("Participants number should not exceed than participants limit"),
    NAME_TAKEN("Course name is taken"),
    STATUS_IS_NOT_ACTIVE("Course status is not ACTIVE"),
    COURSE_MEMBER_IS_ALREADY_ENROLLED("Course member is already enrolled"),
    COURSE_WAS_ALREADY_INACTIVE("Course was already INACTIVE");

    private final String info;


    ErrorInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}