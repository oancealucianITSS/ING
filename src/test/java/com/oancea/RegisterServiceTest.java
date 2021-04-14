package com.oancea;

import com.oancea.domain.UserEntity;
import com.oancea.dto.UserDto;
import com.oancea.dto.request.UserRegisterRequest;
import com.oancea.dto.response.ResponseCreateUser;
import com.oancea.mapper.UserDTOToEntityMapper;
import com.oancea.mapper.UserEntityToDTOMapper;
import com.oancea.repository.UserRepository;
import com.oancea.service.register.impl.RegisterServiceImpl;
import com.oancea.utils.SensitiveLog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.oancea.service.register.impl.RegisterServiceImpl.USER_ALREADY_EXISTS;
import static com.oancea.utils.Constants.*;

@RunWith(SpringRunner.class)
public class RegisterServiceTest {

    public static final String INVALID_EMAIL_ADRESS = "INVALID_EMAIL_ADRESS";

    private UserDTOToEntityMapper userDTOToEntityMapper = Mappers.getMapper(UserDTOToEntityMapper.class);

    private UserEntityToDTOMapper userEntityToDTOMapper = Mappers.getMapper(UserEntityToDTOMapper.class);

    @Mock
    private SensitiveLog sensitiveLog;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterServiceImpl registerService = new RegisterServiceImpl();

    @Test
    public void createUserWhenUserIsValidReceiveOK() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest();
        userRequest.setEmail(EMAIL_TEST);
        userRequest.setFirstName(FIRSTNAME_TEST);
        userRequest.setLastName(LASTNAME_TEST);
        userRequest.setIsActive(true);
        userRequest.setPassword(TEST);
        userRequest.setAge(10);

        //UserEntity mapped
        UserEntity userEntityMapped = userDTOToEntityMapper.mapUserDTOToEntity(userRequest);

        //Mocking findByEmail method to return Optional.empty()
        Mockito.when(this.userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());

        //Mocking call to save UserEntity in db
        Mockito.when(userRepository.save(userEntityMapped)).thenReturn(userEntityMapped);

        //Calling save UserEntity to db mocked
        UserEntity userEntitySaved = userRepository.save(userEntityMapped);

        UserDto userDto = userEntityToDTOMapper.mapUserEntityToDTO(userEntitySaved);

        //CreateUserResponse mocked as ResponseEntity
        ResponseCreateUser responseCreateUserMOcked = new ResponseCreateUser(200, SUCCESS, userDto);

        //Call createUser method with Request given
        ResponseCreateUser createUserResponse = registerService.createUser(userRequest);
        //Test
        Assert.assertEquals(createUserResponse, responseCreateUserMOcked);
    }

    @Test
    public void conflictUserAlreadyExists() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest();
        userRequest.setEmail(EMAIL_TEST);
        userRequest.setFirstName(FIRSTNAME_TEST);
        userRequest.setLastName(LASTNAME_TEST);
        userRequest.setIsActive(true);
        userRequest.setPassword(TEST);
        userRequest.setAge(10);

        //UserEntity mapped
        UserEntity userEntityMapped = userDTOToEntityMapper.mapUserDTOToEntity(userRequest);

        //Mocking findByEmail
        Mockito.when(this.userRepository.findByEmail(userRequest.getEmail().toLowerCase())).thenReturn(Optional.ofNullable(userEntityMapped));

        //CreateUserResponse mocked as ResponseEntity
        ResponseCreateUser responseCreateUserMocked = new ResponseCreateUser(409, USER_ALREADY_EXISTS, null);

        //Call createUser method with Request given
        ResponseCreateUser createUserResponse = registerService.createUser(userRequest);
        //Test
        Assert.assertEquals(createUserResponse, responseCreateUserMocked);
    }

    @Test
    public void badRequestInvalidEmailAdress() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest();
        userRequest.setEmail("EMAIL_TEST"); // for INVALID_EMAIL_ADRESS flow
        userRequest.setFirstName(FIRSTNAME_TEST);
        userRequest.setLastName(LASTNAME_TEST);
        userRequest.setIsActive(true);
        userRequest.setPassword(TEST);
        userRequest.setAge(10);

        //UserEntity mapped
        UserEntity userEntityMapped = userDTOToEntityMapper.mapUserDTOToEntity(userRequest);
        //Mocking findByEmail method to return null
        Mockito.when(this.userRepository.findByEmail(userRequest.getEmail().toLowerCase())).thenReturn(Optional.ofNullable(userEntityMapped));

        //CreateUserResponse mocked as ResponseEntity
        ResponseCreateUser responseCreateUserMocked = new ResponseCreateUser(400, INVALID_EMAIL_ADRESS, null);

        //Call createUser method with Request given
        ResponseCreateUser createUserResponse = registerService.createUser(userRequest);
        //Test
        Assert.assertEquals(createUserResponse, responseCreateUserMocked);
    }
}