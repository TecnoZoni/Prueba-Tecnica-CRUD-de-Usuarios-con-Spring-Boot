package com.user.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String nombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String apellido1;

    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String apellido2;

    @Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.0-9]{6,12}$",
            message = "Número de teléfono inválido")
    private String telefono;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(unique = true, length = 100)
    private String email;

    @Builder.Default
    private Boolean active = true;

    private LocalDateTime lastAccess;

    private LocalDateTime lastModified;

    @PrePersist
    @PreUpdate
    private void updateTimestamps() {
        this.lastModified = LocalDateTime.now();
    }

    // Método para actualizar último acceso
    public void updateLastAccess() {
        this.lastAccess = LocalDateTime.now();
    }
}
