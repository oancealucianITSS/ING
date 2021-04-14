package com.oancea.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

    private Boolean isActive;

    private Integer age;
}
