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
    ResponseEntity<List<Course>> getCourses() {
        return courseService.getCourses();
    }

    @GetMapping("/{code}")
    ResponseEntity<Course> getStudent(@PathVariable String code) {
        return courseService.getCourse(code);
    }

    @PostMapping
    ResponseEntity<Course> addStudent(@RequestBody @Valid Course toCreate) {
        return courseService.addCourse(toCreate);
    }
}
