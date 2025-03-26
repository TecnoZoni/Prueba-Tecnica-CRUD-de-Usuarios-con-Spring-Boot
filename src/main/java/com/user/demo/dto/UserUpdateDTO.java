package com.user.demo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    private String nombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 50)
    private String apellido1;

    @Size(max = 50)
    private String apellido2;

    @Pattern(
    regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.0-9]{6,12}$",
    message = "Formato inválido. Ejemplos válidos: +34 612345678, 955123456, (0034)611223344"
)
    private String telefono;

    @Email
    private String email;  // Campo agregado para validación
}
