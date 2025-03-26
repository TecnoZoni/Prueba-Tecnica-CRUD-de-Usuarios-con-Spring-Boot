package com.user.demo.controller;

import com.user.demo.dto.*;
import com.user.demo.exception.EmailAlreadyExistsException;
import com.user.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final int DEFAULT_PAGE_SIZE = 5;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. Listado de usuarios con paginación
    @GetMapping
    public String listUsers(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("active") Optional<Boolean> activeFilter,
            Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<UserResponseDTO> usersPage = userService.getAllUsers(
                PageRequest.of(currentPage - 1, pageSize),
                activeFilter.orElse(null)
        );

        model.addAttribute("usersPage", usersPage);
        model.addAttribute("activeFilter", activeFilter.orElse(null));

        // Para la paginación en la vista
        int totalPages = usersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "users/list";
    }

    // 2. Mostrar formulario de creación
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userCreationDTO", new UserCreationDTO());
        return "users/create-form";
    }

    // 3. Procesar creación de usuario
    @PostMapping("/create")
    public String createUser(
            @Valid @ModelAttribute("userCreationDTO") UserCreationDTO userCreationDTO,
            BindingResult result, // ¡Este parámetro captura los errores de validación!
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Si hay errores, regresa al formulario MOSTRANDO LOS MENSAJES
            return "users/create-form";
        }

        try {
            userService.createUser(userCreationDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario creado exitosamente");
        } catch (EmailAlreadyExistsException e) {
            // Manejo específico para email duplicado (validación adicional del servicio)
            result.rejectValue("email", "error.userCreationDTO", e.getMessage());
            return "users/create-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado: " + e.getMessage());
        }

        return "redirect:/users";
    }

    // 4. Mostrar formulario de edición
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserResponseDTO user = userService.getUserById(id);
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        // Mapear los campos necesarios al DTO de actualización
        updateDTO.setNombre(user.getNombre());
        updateDTO.setApellido1(user.getApellido1());
        updateDTO.setApellido2(user.getApellido2());
        updateDTO.setTelefono(user.getTelefono());
        updateDTO.setEmail(user.getEmail());

        model.addAttribute("userId", id);
        model.addAttribute("user", updateDTO);
        return "users/edit-form";
    }

    // 5. Procesar actualización de usuario
    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("user") UserUpdateDTO userUpdateDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "users/edit-form";
        }

        try {
            userService.updateUser(id, userUpdateDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/edit/" + id;
        }

        return "redirect:/users";
    }

    // 6. Eliminar usuario (eliminación lógica)
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    // 7. Marcar último acceso
    @PostMapping("/last-access/{id}")
    public String updateLastAccess(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.updateLastAccess(id);
            redirectAttributes.addFlashAttribute("successMessage", "Último acceso actualizado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}
