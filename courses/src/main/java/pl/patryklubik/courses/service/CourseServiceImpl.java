package pl.patryklubik.courses.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.patryklubik.courses.model.Course;
import pl.patryklubik.courses.model.CourseMember;
import pl.patryklubik.courses.repository.CourseRepository;
import pl.patryklubik.courses.model.dto.StudentDto;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Create by Patryk Łubik on 07.08.2021.
 */

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentServiceClient studentServiceClient;

    public CourseServiceImpl(CourseRepository courseRepository, StudentServiceClient studentServiceClient) {
        this.courseRepository = courseRepository;
        this.studentServiceClient = studentServiceClient;
    }


    public ResponseEntity<List<Course>> getCourses(Course.Status status) {
        if (status != null) {
            return ResponseEntity.ok(courseRepository.findAllByStatus(status));
        }
        return ResponseEntity.ok(courseRepository.findAll());
    }

    public ResponseEntity<Course> getCourse(String code) {
        return courseRepository.findById(code).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<StudentDto>> getCourseMembers(String code) {

        return courseRepository.findById(code).map(course -> {
            List<@NotNull String> emailsMembers = getCourseMembersEmails(course);
            return ResponseEntity.ok().body(studentServiceClient.getStudentsByEmails(emailsMembers));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private List<@NotNull String> getCourseMembersEmails(Course course) {
        return course.getCourseMembers().stream()
                .map(CourseMember::getEmail).collect(Collectors.toList());
    }

    public ResponseEntity<Course> addCourse(Course toCreate) {

        toCreate.validateCourse();
        validateCourseNameExistsInDatabase(toCreate.getName());
        toCreate.validateCourse();
        Course result = courseRepository.save(toCreate);

        return ResponseEntity.created(URI.create("/" + result.getCode())).body(result);
    }

    public ResponseEntity<Course> addCourseMember(Long newMemberId, String code) {

          return courseRepository.findById(code).map(course -> {
            validateCourseStatusIsActive(course);
            StudentDto student = studentServiceClient.getStudentById(newMemberId);
            CourseMember courseMember = new CourseMember(student.getEmail());
            validateCourseMembersIsEnrolled(course, courseMember);

            course.getCourseMembers().add(courseMember);
            course.setParticipantsNumber(course.getParticipantsNumber() + 1);
            if(course.getParticipantsLimit() == course.getParticipantsNumber()) {
                course.setStatus(Course.Status.FULL);
            }
            return ResponseEntity.ok().body(courseRepository.save(course));

         }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> finishEnroll(String code) {
        return courseRepository.findById(code)
                .map(course -> {
                    validateCourseStatusIsInactive(course);
                    course.setStatus(Course.Status.INACTIVE);
                    courseRepository.save(course);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteCourse(String code) {
        return courseRepository.findById(code)
                .map(course -> {
                    course.setStatus(Course.Status.INACTIVE);
                    courseRepository.save(course);
                    courseRepository.deleteById(code);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Course> putCourse(String code, Course course) {

        course.validateCourse();

        return courseRepository.findById(code)
                .map(courseFromDb -> {
                    if (!courseFromDb.getName().equals(course.getName())) {
                        validateCourseNameExistsInDatabase(course.getName());
                    }
                    courseFromDb.setName(course.getName());
                    courseFromDb.setDescription(course.getDescription());
                    courseFromDb.setEndDate(course.getStartDate());
                    courseFromDb.setEndDate(course.getEndDate());
                    courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
                    courseFromDb.setParticipantsNumber(course.getParticipantsNumber());
                    courseFromDb.setStatus(course.getStatus());

                    return ResponseEntity.ok().body(courseRepository.save(courseFromDb));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void validateCourseNameExistsInDatabase(String name) {
        if(courseRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course name is taken");
        }
    }

    private void validateCourseStatusIsActive(Course course) {
        if(course.getStatus() != Course.Status.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course status is not ACTIVE");
        }
    }

    private void validateCourseMembersIsEnrolled(Course course, CourseMember courseMemberToAdd) {
        if(course.getCourseMembers().contains(courseMemberToAdd)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course member is already enrolled");
        }
    }

    private void validateCourseStatusIsInactive(Course course) {
        if(course.getStatus() == Course.Status.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course was already INACTIVE");
        }
    }
}
