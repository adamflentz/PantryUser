package com.adamlentz.pantryuser.controller;

import com.adamlentz.pantryuser.entities.AuthType;
import com.adamlentz.pantryuser.entities.User;
import com.adamlentz.pantryuser.payload.AuthControllerResponse;
import com.adamlentz.pantryuser.payload.AuthResponse;
import com.adamlentz.pantryuser.payload.LoginRequest;
import com.adamlentz.pantryuser.payload.SignUpRequest;
import com.adamlentz.pantryuser.repositories.UserRepository;
import com.adamlentz.pantryuser.security.CurrentUser;
import com.adamlentz.pantryuser.security.UserPrincipal;
import com.adamlentz.pantryuser.services.TokenProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private TokenProviderService tokenProviderService;

    @Autowired
    public AuthController(final AuthenticationManager authenticationManager,
                          final UserRepository userRepository,
                          final PasswordEncoder passwordEncoder,
                          final TokenProviderService tokenProviderService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProviderService = tokenProviderService;
    }

    @DeleteMapping(path = "/logout")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity<?> logoutUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(new AuthControllerResponse(true, "User logged out successfully"));
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProviderService.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail()))
        {
            throw new UnsupportedOperationException("Email already in use.");
        }

        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setAuthType(AuthType.LOCAL);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/usr/me").buildAndExpand(result.getUserId()).toUri();

        return ResponseEntity.created(location).body(new AuthControllerResponse(true, "User registered successfully"));
    }
}
