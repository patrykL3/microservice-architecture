package pl.patryklubik.courses.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.patryklubik.courses.exception.ErrorInfo;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Create by Patryk Łubik on 07.08.2021.
 */

@Getter
@Setter
@Document
public class Course {

    @Id
    private String code;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @Future
    private LocalDateTime startDate;
    @NotNull
    @Future
    private LocalDateTime endDate;
    @Min(0)
    private Long participantsLimit;
    @NotNull
    @Min(0)
    private Long participantsNumber;

    @NotNull
    private Status status;

    private List<CourseMember> courseMembers = new ArrayList<>();

    public enum Status {
        ACTIVE,
        INACTIVE,
        FULL
    }

    public void validateCourse() {
        validateCourseDate();
        validateParticipantsLimit();
        validateStatus();
    }

    private void validateCourseDate() {
        if(startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorInfo.COURSE_DATES_INCORRECT.getInfo());
        }
    }

    private void validateParticipantsLimit() {
        if(participantsNumber > participantsLimit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorInfo.PARTICIPANTS_NUMBER_EXCEED_LIMIT.getInfo());
        }

    }

    private void validateStatus() {
        if(status == Course.Status.FULL && participantsNumber < participantsLimit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorInfo.PARTICIPANTS_NUMBER_IS_NOT_FULL.getInfo());
        }
    }
}
