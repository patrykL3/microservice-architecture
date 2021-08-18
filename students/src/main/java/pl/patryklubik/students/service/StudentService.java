package pl.patryklubik.students.service;

import org.springframework.http.ResponseEntity;
import pl.patryklubik.students.model.Student;

import java.util.List;


/**
 * Create by Patryk Łubik on 04.08.2021.
 */

public interface StudentService {

    ResponseEntity<List<Student>> getStudents(Student.Status status);

    ResponseEntity<Student> getStudent(Long id);

    ResponseEntity<Student> addStudent(Student student);

    ResponseEntity<?> deleteStudent(Long id);

    ResponseEntity<Student> putStudent(Long id, Student student);

    ResponseEntity<Student> patchStudent(Long id, Student student);

    ResponseEntity<List<Student>> getStudentsByEmails(List<String> emails);
}
