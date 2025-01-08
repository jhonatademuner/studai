package com.studai.controller.user;

import com.studai.domain.user.User;
import com.studai.domain.user.dto.UserLoginDTO;
import com.studai.domain.user.dto.UserRegisterDTO;
import com.studai.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
