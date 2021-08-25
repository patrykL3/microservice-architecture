package pl.patryklubik.students.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;


/**
 * Create by Patryk Łubik on 25.07.2021.
 */

@Getter
@Setter
@Entity (name= "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotEmpty
    @Size(min = 3)
    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}