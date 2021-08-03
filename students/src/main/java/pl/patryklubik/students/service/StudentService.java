package pl.patryklubik.students.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import pl.patryklubik.students.model.Student;
import pl.patryklubik.students.model.StudentRepository;

import java.net.URI;
import java.util.List;


/**
 * Create by Patryk Łubik on 29.07.2021.
 */

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public ResponseEntity<List<Student>> getStudents () {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    public ResponseEntity<Student> getStudent(Long id) {
        return studentRepository.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Student> addStudent(Student toCreate) {
        Student result = studentRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    public ResponseEntity<?> deleteStudent(Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.delete(student);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Student> putStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(studentFromDb -> {
                    studentFromDb.setFirstName(student.getFirstName());
                    studentFromDb.setLastName(student.getLastName());
                    studentFromDb.setEmail(student.getEmail());
                    return ResponseEntity.ok().body(studentRepository.save(studentFromDb));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Student> patchStudent(Long id, Student student) {
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
