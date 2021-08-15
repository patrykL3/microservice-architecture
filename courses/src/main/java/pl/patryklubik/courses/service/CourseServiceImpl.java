package pl.patryklubik.courses.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;
import pl.patryklubik.courses.model.Course;
import pl.patryklubik.courses.model.CourseMember;
import pl.patryklubik.courses.repository.CourseRepository;
import pl.patryklubik.courses.model.dto.StudentDto;

import java.net.URI;
import java.util.List;


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

    public ResponseEntity<List<CourseMember>> getCourseMembers(String code) {
        return courseRepository.findById(code).map(Course::getCourseMembers).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    public ResponseEntity<Course> addCourse(Course toCreate) {

        validateCourseDataIsCorrect(toCreate);
        validateCourseNameExists(toCreate.getName());

        Course result = courseRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getCode())).body(result);
    }

    public ResponseEntity<Course> addCourseMember(Long newMemberId, String code) {

          return courseRepository.findById(code).map(course -> {
            validateCourseStatus(course);
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


    private void validateCourseStatus(Course course) {
        if(course.getStatus() != Course.Status.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course status is not ACTIVE");
        }
    }

    private void validateCourseMembersIsEnrolled(Course course, CourseMember courseMemberToAdd) {
        if(course.getCourseMembers().contains(courseMemberToAdd)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course member is already enrolled");
        }
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

        validateCourseDataIsCorrect(course);

        return courseRepository.findById(code)
                .map(courseFromDb -> {
                    if (!courseFromDb.getName().equals(course.getName())) {
                        validateCourseNameExists(course.getName());
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

    public ResponseEntity<Course> patchCourse(String code, Course course) {

        validateCourseDataIsCorrect(course);

        return courseRepository.findById(code)
                .map(courseFromDb -> {
                    if (!ObjectUtils.isEmpty(course.getDescription())) {
                        courseFromDb.setDescription(course.getDescription());
                    }
                    if (!ObjectUtils.isEmpty(course.getStartDate())) {
                        courseFromDb.setEndDate(course.getStartDate());
                    }
                    if (!ObjectUtils.isEmpty(course.getEndDate())) {
                        courseFromDb.setEndDate(course.getEndDate());
                    }
                    if (!ObjectUtils.isEmpty(course.getParticipantsLimit())) {
                        courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
                    }
                    if (!ObjectUtils.isEmpty(course.getParticipantsNumber())) {
                        courseFromDb.setParticipantsNumber(course.getParticipantsNumber());
                    }

                    return ResponseEntity.ok().body(courseRepository.save(courseFromDb));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    private void validateCourseNameExists(String name) {
        if(courseRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course name is taken");
        }
    }

    private void validateCourseDataIsCorrect(Course course) {
        if((course.getStartDate()).isAfter(course.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date should not be after end date");
        }
        if(course.getParticipantsNumber() > course.getParticipantsLimit()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participants number should not exceed than participants limit");
        }
        if(course.getStatus() == Course.Status.FULL && course.getParticipantsNumber() < course.getParticipantsLimit()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participants number shows that course is not FULL");
        }
    }
}
