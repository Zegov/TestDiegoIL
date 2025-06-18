package com.sermaluc.prueba.apiuser.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.sermaluc.prueba.apiuser.model.Telefono;
import com.sermaluc.prueba.apiuser.model.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Telefono testPhone;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setNombre("Eusebio Jimenez");
        testUser.setEmail("euji@prueba4.cl");
        testUser.setPassword("Password1&");
        testUser.setCreado(LocalDateTime.now());
        testUser.setModificado(LocalDateTime.now());
        testUser.setUltimoLogin(LocalDateTime.now());
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setEstaActivo(true);

        testPhone = new Telefono();
        testPhone.setNumeroFono("1234567");
        testPhone.setCodCiudad("1");
        testPhone.setCodPais("57");
    }

    @Test
    void whenSaveUser_thenUserIsPersistedWithAllFields() {
        // Given
        testUser.setTelefonos(Arrays.asList(testPhone));

        // When
        User savedUser = userRepository.save(testUser);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("Eusebio Jimenez", savedUser.getNombre());
        assertEquals("euji@prueba4.cl", savedUser.getEmail());
        assertEquals(testUser.getPassword(), savedUser.getPassword());
        assertNotNull(savedUser.getCreado());
        assertNotNull(savedUser.getModificado());
        assertNotNull(savedUser.getUltimoLogin());
        assertNotNull(savedUser.getToken());
        assertTrue(savedUser.isEstaActivo());
        assertEquals(1, savedUser.getTelefonos().size());
    }

    @Test
    void whenFindByEmail_thenReturnUser() {
        // Given
        testUser.setNombre("Maria Lopez");
        testUser.setEmail("malo@prueba5.cl");
        testUser.setPassword("Password2!");
        userRepository.save(testUser);

        // When
        User foundUser = userRepository.findByEmail("malo@prueba5.cl");

        // Then
        assertNotNull(foundUser);
        assertEquals("Maria Lopez", foundUser.getNombre());
    }

    @Test
    void whenFindByNonExistentEmail_thenReturnNull() {
        // When
        User foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertNull(foundUser);
    }

    @Test
    void whenSaveUserWithDuplicateEmail_thenThrowException() {
        // Given
        userRepository.save(testUser);

        User duplicateUser = new User();
        duplicateUser.setId(UUID.randomUUID());
        duplicateUser.setNombre("Maria Lopez");
        duplicateUser.setEmail("euji@prueba4.cl"); // mismo email que testUser
        duplicateUser.setPassword("Password2!");

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(duplicateUser);
        });
    }

    @Test
    void whenSaveUserWithPhones_thenPhonesArePersistedCorrectly() {
        // Given
        Telefono phone1 = new Telefono();
        phone1.setNumeroFono("1234567");
        phone1.setCodCiudad("1");
        phone1.setCodPais("57");

        Telefono phone2 = new Telefono();
        phone2.setNumeroFono("7654321");
        phone2.setCodCiudad("2");
        phone2.setCodPais("58");

        testUser.setTelefonos(Arrays.asList(phone1, phone2));

        // When
        User savedUser = userRepository.save(testUser);

        // Then
        assertEquals(2, savedUser.getTelefonos().size());
        assertTrue(savedUser.getTelefonos().stream()
            .anyMatch(p -> p.getNumeroFono().equals("1234567")));
        assertTrue(savedUser.getTelefonos().stream()
            .anyMatch(p -> p.getNumeroFono().equals("7654321")));
    }

    @Test
    void whenUpdateUser_thenFieldsAreUpdated() {
        // Given
        User savedUser = userRepository.save(testUser);
        
        // When
        savedUser.setNombre("Updated Name");
        savedUser.setModificado(LocalDateTime.now());
        User updatedUser = userRepository.save(savedUser);

        // Then
        assertEquals("Updated Name", updatedUser.getNombre());
        assertTrue(updatedUser.getModificado().isAfter(updatedUser.getCreado()));
    }

    @Test
    void whenDeleteUser_thenUserIsRemoved() {
        // Given
        User savedUser = userRepository.save(testUser);

        // When
        userRepository.delete(savedUser);

        // Then
        assertFalse(userRepository.findById(savedUser.getId()).isPresent());
    }

    @Test
    void whenFindAllUsers_thenReturnUsersList() {
        // Given
        User user1 = testUser;
        
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setNombre("Maria Lopez");
        user2.setEmail("malo@prueba5.cl");
        user2.setPassword("Password2!");
        user2.setCreado(LocalDateTime.now());
        user2.setModificado(LocalDateTime.now());
        user2.setUltimoLogin(LocalDateTime.now());
        user2.setToken(UUID.randomUUID().toString());
        user2.setEstaActivo(true);

        userRepository.saveAll(Arrays.asList(user1, user2));

        // When
        List<User> users = userRepository.findAll();

        // Then
        assertEquals(2, users.size());
    }
}
