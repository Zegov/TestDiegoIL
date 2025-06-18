package com.sermaluc.prueba.apiuser.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sermaluc.prueba.apiuser.dto.UserDTO;
import com.sermaluc.prueba.apiuser.dto.UserRequestDTO;
import com.sermaluc.prueba.apiuser.exception.EmailExistsException;
import com.sermaluc.prueba.apiuser.exception.UserRegistrationException;
import com.sermaluc.prueba.apiuser.exception.ValidationException;
import com.sermaluc.prueba.apiuser.mapper.UserMapper;
import com.sermaluc.prueba.apiuser.model.User;
import com.sermaluc.prueba.apiuser.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(
        rollbackFor = {
            UserRegistrationException.class,
            EmailExistsException.class, 
            ValidationException.class
        }
    )

    public UserDTO registerUser(UserRequestDTO userRequest) {
        validateUserData(userRequest);

        if (userRepository.findByEmail(userRequest.getEmail()) != null) {
            throw new EmailExistsException("El correo ya se encuentra registrado");
        }

        try {            
            User user = userMapper.toEntity(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setCreado(LocalDateTime.now());
            user.setModificado(LocalDateTime.now());
            user.setUltimoLogin(LocalDateTime.now());
            user.setToken(UUID.randomUUID().toString());
            user.setEstaActivo(true);

            return userMapper.toDTO(userRepository.save(user));

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario", e);
        }
    }

    private void validateUserData(UserRequestDTO request) {
        if (request == null) {
            throw new ValidationException("Los datos del usuario no pueden ser nulos");
        }
    }
    
}
