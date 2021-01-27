package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.exceptions.NotFoundException;
import com.geekbrains.geekspring.repositories.RoleRepository;
import com.geekbrains.geekspring.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RequestMapping("/user")
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        logger.info("Edit user with id {}", id);

        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", userService.findById(id)
                .orElseThrow(NotFoundException::new));
        return "user_form";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_form";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        logger.info("Delete user with id {}", id);
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}