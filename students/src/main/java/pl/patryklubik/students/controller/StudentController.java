package pl.patryklubik.students.controller;

import org.springframework.http.ResponseEntity;
import pl.patryklubik.students.service.StudentService;
import pl.patryklubik.students.service.StudentServiceImpl;
import pl.patryklubik.students.model.Student;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Create by Patryk Łubik on 25.07.2021.
 */

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    ResponseEntity<List<Student>> getStudents(@RequestParam(required = false) Student.Status status) {
        return studentService.getStudents(status);
    }

    @GetMapping("/{id}")
    ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping("/emails")
    ResponseEntity<List<Student>> getStudentsByEmails(@RequestBody List<String> emails) {
        return studentService.getStudentsByEmails(emails);
    }

    @PostMapping
    ResponseEntity<Student> addStudent(@RequestBody @Valid Student toCreate) {
        return studentService.addStudent(toCreate);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @PutMapping("/{id}")
    ResponseEntity<Student> putStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        return studentService.putStudent(id, student);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Student> patchStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.patchStudent(id, student);
    }
}
