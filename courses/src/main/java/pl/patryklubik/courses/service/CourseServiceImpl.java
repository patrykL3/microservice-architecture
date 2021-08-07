package pl.patryklubik.courses.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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


    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    public ResponseEntity<Course> getCourse(String code) {
        return courseRepository.findByCode(code).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Course> addCourse(Course toCreate) {
        Course result = courseRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getCode())).body(result);
    }
}
