package com.sermaluc.prueba.apiuser.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Datos para el registro de un nuevo usuario")
@Data
public class UserRequestDTO {

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$", message = "El nombre solo debe contener letras y espacios")
    private String nombre;

    @Schema(description = "Correo electrónico del usuario", example = "edmundo.test@sermaluc.cl")
    @NotBlank(message = "El email es obligatorio")
    @Email(regexp = "${user.email.regex}", message = "El formato del email no es válido")
    @Size(max = 50, message = "El email no debe superar los 50 caracteres")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "Password1!")
    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "${user.password.regex}", 
             message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String password;

    @Schema(description = "Lista de teléfonos del usuario")
    @NotEmpty(message = "Debe proporcionar al menos un teléfono")
    private List<PhoneRequestDTO> phones;
}
