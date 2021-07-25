package pl.patryklubik.students.adapter;

import pl.patryklubik.students.model.Student;
import pl.patryklubik.students.model.StudentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Create by Patryk Łubik on 25.07.2021.
 */
@Repository
interface SqlStudentRepository extends StudentRepository, JpaRepository<Student, Long> {
}
