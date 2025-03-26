package com.user.demo.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String nombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String apellido1;

    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String apellido2;

    @Pattern(
            regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.0-9]{6,12}$",
            message = "Formato inválido. Ejemplos válidos: +34 612345678, 955123456, (0034)611223344"
    )
    private String telefono;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    private Boolean active;

    private LocalDateTime lastAccess;
}
