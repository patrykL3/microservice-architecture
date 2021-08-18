package pl.patryklubik.students.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findById(Long id);
    List<Student> findAll();
    Optional<List<Student>> findAllByEmailIn(List<String> emails);
    List<Student> findAllByStatus(Student.Status status);
    Student save(Student entity);
    boolean existsByEmail(String username);
}
