package pl.patryklubik.courses.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CourseMember {

    @NotNull
    private LocalDateTime dateEnrolled;
    @NotNull
    private String email;

    public CourseMember(String email) {
        this.email = email;
        this.dateEnrolled = LocalDateTime.now();
    }

    public LocalDateTime getDateEnrolled() {
        return dateEnrolled;
    }

    public String getEmail() {
        return email;
    }
}
