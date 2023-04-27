package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String printUsers(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("user", userService.findUserByUsername(principal.getName()).orElseThrow());
        return "admin/admin";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") @Valid User user, @RequestParam("rolesList") String[] roles,
                          BindingResult br) {
        if (br.hasErrors()) {
            return "admin/admin";
        }
        userService.addUser(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/get")
    public String getUser(ModelMap model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/admin";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult br,
                             @PathVariable("id") Long id, @RequestParam("rolesList") String[] roles) {
        if (br.hasErrors()) {
            return "admin/admin";
        }
        userService.updateUser(id, user, roles);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String dropUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
