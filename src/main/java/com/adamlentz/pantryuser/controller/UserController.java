package com.adamlentz.pantryuser.controller;

import com.adamlentz.pantryuser.repositories.UserRepository;
import com.adamlentz.pantryuser.security.CurrentUser;
import com.adamlentz.pantryuser.security.UserPrincipal;
import com.adamlentz.pantryuser.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController(value = "user")
@RequestMapping("/user")
public class UserController {

    CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserController(final CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping(path = "/getUser/{user}")
    public @ResponseBody
    UserDetails getUser(@PathVariable Long user) {
        return customUserDetailsService.loadUserById(user);
    }

    @GetMapping("/me")
    public UserDetails getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return customUserDetailsService.loadUserById(userPrincipal.getId());
    }


}