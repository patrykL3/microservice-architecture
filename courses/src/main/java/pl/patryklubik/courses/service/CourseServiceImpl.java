package pl.patryklubik.courses.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;
import pl.patryklubik.courses.model.Course;
import pl.patryklubik.courses.model.CourseRepository;

import java.net.URI;
import java.util.List;

/**
 * Create by Patryk Łubik on 07.08.2021.
 */

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    public ResponseEntity<List<Course>> getCourses(Course.Status status) {
        if (status != null) {
            return ResponseEntity.ok(courseRepository.findAllByStatus(status));
        }
        return ResponseEntity.ok(courseRepository.findAll());
    }

    public ResponseEntity<Course> getCourse(Long id) {
        return courseRepository.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Course> addCourse(Course toCreate) {

        validateCourseDataIsCorrect(toCreate);
        validateCourseNameExists(toCreate.getName());

        Course result = courseRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    public ResponseEntity<?> deleteCourse(Long id) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setStatus(Course.Status.INACTIVE);
                    courseRepository.save(course);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Course> putCourse(Long id, Course course) {

        validateCourseDataIsCorrect(course);

        return courseRepository.findById(id)
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

    public ResponseEntity<Course> patchCourse(Long id, Course course) {

        validateCourseDataIsCorrect(course);

        return courseRepository.findById(id)
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
