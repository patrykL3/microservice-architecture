package pl.patryklubik.courses.adapter;

import pl.patryklubik.courses.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.patryklubik.courses.model.CourseRepository;


/**
 * Create by Patryk Łubik on 07.08.2021.
 */
@Repository
interface SQLCourseRepository extends CourseRepository, JpaRepository<Course, String> {
}
