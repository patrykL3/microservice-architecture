package pl.patryklubik.students.exception;

/**
 * Create by Patryk Łubik on 25.08.2021.
 */
public enum ErrorInfo {
    INACTIVE_STUDENT("Student is not active"),
    EMAIL_IS_TAKEN("Email is taken");

    private final String info;


    ErrorInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
