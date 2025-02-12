package com.studai.controller.user;

import com.studai.domain.user.User;
import com.studai.domain.user.dto.UserLoginDTO;
import com.studai.domain.user.dto.UserRegisterDTO;
import com.studai.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> updatePassword(@RequestBody UserLoginDTO user, @RequestParam String newPassword){
        userService.updatePassword(user, newPassword);
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

}
