package pl.patryklubik.courses.service;


import org.springframework.http.ResponseEntity;
import pl.patryklubik.courses.model.Course;
import pl.patryklubik.courses.model.dto.StudentDto;

import java.util.List;


/**
 * Create by Patryk Łubik on 07.08.2021.
 */

public interface CourseService {

    ResponseEntity<List<Course>> getCourses(Course.Status status);

    ResponseEntity<Course> getCourse(String code);

    ResponseEntity<Course> addCourse(Course course);

    ResponseEntity<?> deleteCourse(String code);

    ResponseEntity<Course> putCourse(String code, Course course);

    ResponseEntity<Course> patchCourse(String code, Course course);

    ResponseEntity<List<StudentDto>> getCourseMembers(String code);

    ResponseEntity<Course> addCourseMember(Long newMemberId, String code);
}
