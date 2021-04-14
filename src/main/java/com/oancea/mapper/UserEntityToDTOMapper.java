package com.oancea.mapper;


import com.oancea.domain.UserEntity;
import com.oancea.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper
public abstract class UserEntityToDTOMapper {
    @Mappings({})
    public abstract UserDto mapUserEntityToDTO(UserEntity userEntity);
}
