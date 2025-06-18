package com.sermaluc.prueba.apiuser.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sermaluc.prueba.apiuser.model.User;
import com.sermaluc.prueba.apiuser.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // Se limpia la BD antes de cada prueba
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        User user = new User();
        user.setNombre("Diego Villegas");
        user.setEmail("juan@prueba.cl");
        user.setPassword("Pelota2!");
        user.setTelefonos(null);;

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.isactive").value(true));
    }

    @Test
    public void testRegisterUserDuplicateEmail() throws Exception {
        User user = new User();
        user.setNombre("Juana de las mercedes");
        user.setEmail("junita@prueba2.cl");
        user.setPassword("Mariposa6!");
        user.setTelefonos(null);

        // Registrar el usuario por primera vez
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        // Intentar registrar el mismo usuario nuevamente
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El correo ya registrado"));
    }

    @Test
    public void testRegisterUserInvalidPassword() throws Exception {
        User user = new User();
        user.setNombre("Pancrasio Fuentes");
        user.setEmail("pancito@prueba3.cl");
        user.setPassword("1234567"); // Contraseña inválida
        user.setTelefonos(null);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La contraseña no cumple con el formato requerido"));
    }

}
