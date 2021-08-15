package pl.patryklubik.courses.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.patryklubik.courses.model.Course;
import pl.patryklubik.courses.model.CourseMember;
import pl.patryklubik.courses.service.CourseService;

import javax.validation.Valid;
import java.util.List;


/**
 * Create by Patryk Łubik on 07.08.2021.
 */

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    ResponseEntity<List<Course>> getCourses(@RequestParam(required = false) Course.Status status) {
        return courseService.getCourses(status);
    }

    @GetMapping("/{code}")
    ResponseEntity<Course> getCourse(@PathVariable String code) {
        return courseService.getCourse(code);
    }

    @GetMapping("/{courseCode}/members")
    ResponseEntity<List<CourseMember>> getCourseMembers(@PathVariable String courseCode) {
        return courseService.getCourseMembers(courseCode);
    }

    @PostMapping
    ResponseEntity<Course> addCourse(@RequestBody @Valid Course toCreate) {
        return courseService.addCourse(toCreate);
    }

    @PostMapping("/{courseCode}/member/{newMemberId}")
    ResponseEntity<Course> addCourseMember(@PathVariable @Valid Long newMemberId, @PathVariable @Valid String courseCode) {
        return courseService.addCourseMember(newMemberId, courseCode);
    }

    @DeleteMapping("/{code}")
    ResponseEntity<?> deleteCourse(@PathVariable String code) {
        return courseService.deleteCourse(code);
    }

    @PutMapping("/{code}")
    ResponseEntity<Course> putCourse(@PathVariable String code, @Valid @RequestBody Course course) {
        return courseService.putCourse(code, course);
    }

    @PatchMapping("/{code}")
    ResponseEntity<Course> patchStudent(@PathVariable String code, @RequestBody Course course) {
        return courseService.patchCourse(code, course);
    }
}
