package pl.patryklubik.courses.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.patryklubik.courses.model.Course;
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

    @GetMapping("/{id}")
    ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return courseService.getCourse(id);
    }

    @PostMapping
    ResponseEntity<Course> addCourse(@RequestBody @Valid Course toCreate) {
        return courseService.addCourse(toCreate);
    }


    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        return courseService.deleteCourse(id);
    }

    @PutMapping("/{id}")
    ResponseEntity<Course> putCourse(@PathVariable Long id, @Valid @RequestBody Course course) {
        return courseService.putCourse(id, course);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Course> patchStudent(@PathVariable Long id, @RequestBody Course course) {
        return courseService.patchCourse(id, course);
    }
}
