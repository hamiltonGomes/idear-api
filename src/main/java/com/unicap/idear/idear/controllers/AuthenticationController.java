package com.unicap.idear.idear.controllers;

import com.unicap.idear.idear.dtos.AuthenticationDto;
import com.unicap.idear.idear.dtos.RegisterDto;
import com.unicap.idear.idear.dtos.UserResponseDto;
import com.unicap.idear.idear.infra.TokenService;
import com.unicap.idear.idear.models.UserModel;
import com.unicap.idear.idear.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (UserModel) userRepository.findByUsername(authenticationDTO.username());
        var userEmail = user.getEmail();
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.ok(new UserResponseDto(token, userEmail));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto registerDTO) {
        if (this.userRepository.findByUsername(registerDTO.username()) != null)
            return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        UserModel userModel = new UserModel(registerDTO.username(), registerDTO.email(), encryptedPassword, registerDTO.role());
        this.userRepository.save(userModel);
        return ResponseEntity.ok().build();
    }
}
