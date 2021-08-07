package pl.patryklubik.courses.service;


import org.springframework.http.ResponseEntity;
import pl.patryklubik.courses.model.Course;

import java.util.List;

/**
 * Create by Patryk Łubik on 07.08.2021.
 */

public interface CourseService {

    ResponseEntity<List<Course>> getCourses();

    ResponseEntity<Course> getCourse(String code);

    ResponseEntity<Course> addCourse(Course course);
}
