package com.oancea.mapper;


import com.oancea.domain.UserEntity;
import com.oancea.dto.request.UserRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;


@Mapper
public abstract class UserDTOToEntityMapper {
    @Mappings({})
    public abstract UserEntity mapUserDTOToEntity(UserRegisterRequest userRegisterRequest);
}



