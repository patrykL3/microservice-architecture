package pl.patryklubik.courses.model;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by Patryk Łubik on 07.08.2021.
 */

public interface CourseRepository extends JpaRepository<Course, String>{

    Optional<Course> findByCode(String code);
    List<Course> findAll();
    Course save(Course entity);
}
