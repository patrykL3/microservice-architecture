package pl.patryklubik.students.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;
import pl.patryklubik.students.model.Student;
import pl.patryklubik.students.model.StudentRepository;

import java.net.URI;
import java.util.List;


/**
 * Create by Patryk Łubik on 29.07.2021.
 */

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public ResponseEntity<List<Student>> getStudents(Student.Status status) {
        if (status != null) {
            return ResponseEntity.ok(studentRepository.findAllByStatus(status));
        }
        return ResponseEntity.ok(studentRepository.findAll());
    }

    public ResponseEntity<Student> getStudent(Long id) {
        studentRepository.findById(id)
                .ifPresent(student -> {
                    if (!Student.Status.ACTIVE.equals(student.getStatus())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student is not active");
                    }
                });

        return studentRepository.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Student> addStudent(Student toCreate) {

        validateStudentEmailExists(toCreate);

        Student result = studentRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    public ResponseEntity<?> deleteStudent(Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setStatus(Student.Status.INACTIVE);
                    studentRepository.save(student);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Student> putStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(studentFromDb -> {
                    if (!studentFromDb.getEmail().equals(student.getEmail())) {
                        validateStudentEmailExists(student);
                    }
                    studentFromDb.setFirstName(student.getFirstName());
                    studentFromDb.setLastName(student.getLastName());
                    studentFromDb.setEmail(student.getEmail());
                    studentFromDb.setStatus(student.getStatus());
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
                    if (!ObjectUtils.isEmpty(student.getStatus())) {
                        studentFromDb.setStatus(student.getStatus());
                    }
                    return ResponseEntity.ok().body(studentRepository.save(studentFromDb));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Student>> getStudentsByEmails(List<String> emails) {

        return studentRepository.findAllByEmailIn(emails).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void validateStudentEmailExists(Student student) {
        if(studentRepository.existsByEmail(student.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is taken");
        }
    }
}
