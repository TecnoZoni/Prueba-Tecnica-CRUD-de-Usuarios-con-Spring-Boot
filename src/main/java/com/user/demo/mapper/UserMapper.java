package com.user.demo.mapper;

import com.user.demo.dto.*;
import com.user.demo.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    // UserDTO
    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

    // UserCreationDTO
    UserCreationDTO userToUserCreationDTO(User user);

    User userCreationDTOToUser(UserCreationDTO dto);

    // UserUpdateDTO
    @Mapping(target = "email", ignore = true) // El email no se debe poder modificar
    @Mapping(target = "active", ignore = true) // El estado active se maneja separadamente
    void updateUserFromUpdateDTO(UserUpdateDTO dto, @MappingTarget User user);

    // UserResponseDTO
    UserResponseDTO userToUserResponseDTO(User user);

    // Método genérico para actualización
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromAnyDTO(Object dto, @MappingTarget User user);
}
