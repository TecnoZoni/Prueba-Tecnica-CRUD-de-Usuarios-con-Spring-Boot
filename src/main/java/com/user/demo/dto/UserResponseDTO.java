package com.user.demo.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String telefono;
    private String email;
    private Boolean active;
    private LocalDateTime lastAccess;
    private LocalDateTime lastModified;
}
