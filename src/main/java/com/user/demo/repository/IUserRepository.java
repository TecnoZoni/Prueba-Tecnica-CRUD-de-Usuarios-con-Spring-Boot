package com.user.demo.repository;

import com.user.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    // Para validar email único en creación/actualización
    boolean existsByEmailAndActiveTrue(String email);

    // Para buscar por email
    Optional<User> findByEmail(String email);

    // Para el filtro de estado (activos/inactivos/todos)
    List<User> findAllByActive(Boolean active);

    // Para actualizar último acceso
    @Modifying
    @Query("UPDATE User u SET u.lastAccess = :lastAccess WHERE u.id = :userId")
    void updateLastAccess(@Param("userId") Long userId, @Param("lastAccess") LocalDateTime lastAccess);

    // Para eliminación lógica (marcar como inactivo)
    @Modifying
    @Query("UPDATE User u SET u.active = false, u.lastModified = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void deactivateUser(@Param("userId") Long userId);

    // Para verificar si un usuario existe y está activo
    boolean existsByIdAndActiveTrue(Long id);

    Page<User> findAllByActive(Boolean active, Pageable pageable);

}
