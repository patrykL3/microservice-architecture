package pl.patryklubik.courses.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pl.patryklubik.courses.model.dto.StudentDto;

import java.util.List;


@RequestMapping("/students")
@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {

    @GetMapping("/{id}")
    StudentDto getStudentById(@PathVariable Long id);

    @PostMapping("/emails")
    List<StudentDto> getStudentsByEmails(@RequestBody List<String> email);
}
