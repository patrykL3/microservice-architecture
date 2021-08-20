package pl.patryklubik.notifications.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


/**
 * Create by Patryk Łubik on 20.08.2021.
 */
@Getter
@Builder
public class EmailDto {

    @NotBlank
    @Email
    private String to;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
