package com.studai.controller.user;

import com.studai.domain.user.User;
import com.studai.domain.user.dto.UserDTO;
import com.studai.domain.user.dto.UserLoginDTO;
import com.studai.domain.user.dto.UserRegisterDTO;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.UserAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody UserRegisterDTO user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDTO user){
        return userService.verify(user);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestParam String userPassword, @RequestParam String newPassword){
        userService.updatePassword(userPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/username")
    public ResponseEntity<Void> updateUsername(@RequestBody UserLoginDTO user, @RequestParam String newUsername){
        userService.updateUsername(user, newUsername);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email")
    public ResponseEntity<Void> updateEmail(@RequestBody UserLoginDTO user, @RequestParam String newEmail){
        userService.updateEmail(user, newEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser(){
        userService.deleteUser();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Void> authenticate(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout in a stateless API requires client-side token removal.");
        response.put("action", "delete_token");

        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<UserDTO> getCurrentUser(){
        return ResponseEntity.ok(UserAssembler.toDTO(userService.getCurrentUser()));
    }

}
