package pl.patryklubik.students.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import pl.patryklubik.students.model.Student;
import pl.patryklubik.students.model.StudentRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Create by Patryk Łubik on 25.07.2021.
 */

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping()
    ResponseEntity<List<Student>> getStudents () {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return studentRepository.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Student> addStudent(@RequestBody @Valid Student toCreate) {
        Student result = studentRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.delete(student);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<Student> putStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        return studentRepository.findById(id)
                .map(studentFromDb -> {
                    studentFromDb.setFirstName(student.getFirstName());
                    studentFromDb.setLastName(student.getLastName());
                    studentFromDb.setEmail(student.getEmail());
                    return ResponseEntity.ok().body(studentRepository.save(studentFromDb));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    ResponseEntity<Student> patchStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentRepository.findById(id)
                .map(studentFromDb -> {
                    if (!ObjectUtils.isEmpty(student.getFirstName())) {
                        studentFromDb.setFirstName(student.getFirstName());
                    }
                    if (!ObjectUtils.isEmpty(student.getLastName())) {
                        studentFromDb.setLastName(student.getLastName());
                    }
                    return ResponseEntity.ok().body(studentRepository.save(studentFromDb));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
