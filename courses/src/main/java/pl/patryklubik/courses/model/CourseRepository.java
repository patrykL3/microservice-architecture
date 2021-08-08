package pl.patryklubik.courses.model;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by Patryk Łubik on 07.08.2021.
 */

public interface CourseRepository extends JpaRepository<Course, Long>{

    Optional<Course> findById(Long id);
    List<Course> findAll();
    List<Course> findAllByStatus(Course.Status status);
    Course save(Course entity);
    boolean existsByName(String name);
}
