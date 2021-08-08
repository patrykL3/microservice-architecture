package pl.patryklubik.courses.service;


import org.springframework.http.ResponseEntity;
import pl.patryklubik.courses.model.Course;

import java.util.List;

/**
 * Create by Patryk Łubik on 07.08.2021.
 */

public interface CourseService {

    ResponseEntity<List<Course>> getCourses(Course.Status status);

    ResponseEntity<Course> getCourse(Long id);

    ResponseEntity<Course> addCourse(Course course);

    ResponseEntity<?> deleteCourse(Long id);

    ResponseEntity<Course> putCourse(Long id, Course course);

    ResponseEntity<Course> patchCourse(Long id, Course course);
}
