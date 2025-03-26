package com.user.demo.service;

import com.user.demo.dto.*;
import com.user.demo.exception.EmailAlreadyExistsException;
import com.user.demo.exception.UserNotFoundException;
import com.user.demo.mapper.UserMapper;
import com.user.demo.model.User;
import com.user.demo.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;

    // 1. Listado de usuarios con paginación y filtro
    public Page<UserResponseDTO> getAllUsers(Pageable pageable, Boolean active) {
        if (active != null) {
            return userRepository.findAllByActive(active, pageable)
                    .map(userMapper::userToUserResponseDTO);
        }
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserResponseDTO);
    }

    // 2. Crear usuario
    @Transactional
    public UserResponseDTO createUser(UserCreationDTO userCreationDTO) {
        // Validar email único
        if (userRepository.existsByEmailAndActiveTrue(userCreationDTO.getEmail())) {
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }

        User user = userMapper.userCreationDTOToUser(userCreationDTO);
        user.setActive(true);
        user.setLastModified(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return userMapper.userToUserResponseDTO(savedUser);
    }

    // 3. Obtener usuario por ID
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.userToUserResponseDTO(user);
    }

    // 4. Actualizar usuario
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        validateEmailUpdate(userUpdateDTO, existingUser);
        updateUserFields(userUpdateDTO, existingUser);

        existingUser.setLastModified(LocalDateTime.now());
        User updatedUser = userRepository.save(existingUser);
        return userMapper.userToUserResponseDTO(updatedUser);
    }

    private void validateEmailUpdate(UserUpdateDTO userUpdateDTO, User existingUser) {
        Optional.ofNullable(userUpdateDTO.getEmail())
                .filter(email -> !email.isEmpty())
                .filter(email -> !email.equals(existingUser.getEmail()))
                .ifPresent(newEmail -> {
                    if (userRepository.existsByEmailAndActiveTrue(newEmail)) {
                        throw new EmailAlreadyExistsException("El email ya está registrado");
                    }
                });
    }

    private void updateUserFields(UserUpdateDTO userUpdateDTO, User user) {
        Optional.ofNullable(userUpdateDTO.getNombre()).ifPresent(user::setNombre);
        Optional.ofNullable(userUpdateDTO.getApellido1()).ifPresent(user::setApellido1);
        Optional.ofNullable(userUpdateDTO.getApellido2()).ifPresent(user::setApellido2);
        Optional.ofNullable(userUpdateDTO.getTelefono()).ifPresent(user::setTelefono);
        Optional.ofNullable(userUpdateDTO.getEmail())
                .filter(email -> !email.isEmpty())
                .ifPresent(user::setEmail);
    }

    // 5. Eliminación lógica (marcar como inactivo)
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsByIdAndActiveTrue(id)) {
            throw new UserNotFoundException("Usuario no encontrado o ya está inactivo");
        }
        userRepository.deactivateUser(id);
    }

    // 6. Marcar último acceso
    @Transactional
    public void updateLastAccess(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado");
        }
        userRepository.updateLastAccess(id, LocalDateTime.now());
    }

    // Método auxiliar para el filtro
    public List<UserResponseDTO> getUsersByStatus(Boolean active) {
        List<User> users = active != null
                ? userRepository.findAllByActive(active)
                : userRepository.findAll();

        return users.stream()
                .map(userMapper::userToUserResponseDTO)
                .toList();
    }
}
