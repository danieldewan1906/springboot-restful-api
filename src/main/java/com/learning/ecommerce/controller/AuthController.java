package com.learning.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ecommerce.entity.Users;
import com.learning.ecommerce.model.JwtResponse;
import com.learning.ecommerce.model.LoginRequest;
import com.learning.ecommerce.model.RefreshTokenRequest;
import com.learning.ecommerce.model.SignUpRequest;
import com.learning.ecommerce.security.jwt.JwtUtils;
import com.learning.ecommerce.security.service.UserDetailsImpl;
import com.learning.ecommerce.security.service.UserDetailsServiceImpl;
import com.learning.ecommerce.service.UsersService;



@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetails;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest request){
        UsernamePasswordAuthenticationToken loginUser = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication =  authenticationManager.authenticate(loginUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String tokens = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshJwtToken(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok().body(new JwtResponse(tokens, refreshToken, principal.getUsername(), principal.getEmail(), principal.getRoles()));
    }

    @PostMapping("/signup")
    public Users signUp(@RequestBody SignUpRequest request){
        Users user = new Users();
        user.setId(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getFullName());
        user.setRoles("user");
        Users createdUser = userService.create(user);
        return createdUser;
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        String token = request.getRefreshToken();
        boolean valid = jwtUtils.validateJwtToken(token);
        if (!valid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetailsImpl, null, userDetailsImpl.getAuthorities());
        String newToken = jwtUtils.generateJwtToken(auth);
        String refreshToken = jwtUtils.generateRefreshJwtToken(auth);
        return ResponseEntity.ok(new JwtResponse(newToken, refreshToken, username, userDetailsImpl.getEmail(), userDetailsImpl.getRoles()));
    }
}
