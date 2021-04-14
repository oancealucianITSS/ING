package com.oancea.dto.response;

import com.oancea.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCreateUser {
    private Integer status;

    private String messageStatus;

    private UserDto payload;

}
