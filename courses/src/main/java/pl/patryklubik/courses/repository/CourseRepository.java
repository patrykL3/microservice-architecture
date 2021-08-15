package pl.patryklubik.courses.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.patryklubik.courses.model.Course;

import java.util.List;
import java.util.Optional;

/**
 * Create by Patryk Łubik on 07.08.2021.
 */

public interface CourseRepository extends MongoRepository<Course, String> {

    Optional<Course> findById(String code);
    List<Course> findAll();
    List<Course> findAllByStatus(Course.Status status);
    Course save(Course entity);
    boolean existsByName(String name);
}
