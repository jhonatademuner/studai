package com.studai.controller.user;

import com.studai.domain.user.dto.*;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.UserAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserAssembler userAssembler;

    public UserController(UserService userService, UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @PostMapping("/v1/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegisterDTO user){
        return ResponseEntity.status(HttpStatus.OK).body(userService.register(user));
    }

    @PostMapping("/v1/login")
    public String login(@RequestBody UserLoginDTO user){
        return userService.verify(user);
    }

    @PutMapping("/v1/me/credentials")
    public ResponseEntity<Void> updateCredentials(@RequestBody UserCredentialsDTO userCredentialsDTO){
        userService.updateCredentials(userCredentialsDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/v1/me")
    public ResponseEntity<Void> deleteUser(@RequestBody UserDeleteDTO userDeleteDTO) {
        userService.deleteUser(userDeleteDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/v1/me/auth")
    public ResponseEntity<Void> authenticate(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/v1/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout in a stateless API requires client-side token removal.");
        response.put("action", "delete_token");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/v1/me")
    public ResponseEntity<UserDTO> getCurrentUser(){
        return ResponseEntity.status(HttpStatus.OK).body(userAssembler.toDto(userService.getCurrentUser()));
    }

}
