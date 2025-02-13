package com.studai.service.user;

import com.studai.domain.user.StudaiUserDetails;
import com.studai.domain.user.User;
import com.studai.domain.user.UserRole;
import com.studai.domain.user.dto.UserLoginDTO;
import com.studai.domain.user.dto.UserRegisterDTO;
import com.studai.repository.user.UserRepository;
import com.studai.service.jwt.JWTService;
import com.studai.utils.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public User register(UserRegisterDTO userDTO){

        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new UserAlreadyExistsException("User already exists with username: " + userDTO.getUsername());
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User entity = User.builder()
            .username(userDTO.getUsername())
            .password(userDTO.getPassword())
            .email(userDTO.getEmail())
            .role(UserRole.STUDENT)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return userRepository.save(entity);
    }

    public String verify(UserLoginDTO user) throws BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }

        throw new BadCredentialsException("Bad credentials");
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StudaiUserDetails userDetails = (StudaiUserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }
    public void updatePassword(String userPassword, String newPassword) throws BadCredentialsException {
        User existingUser = this.getCurrentUser();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(existingUser.getUsername(), userPassword));
        if(!authentication.isAuthenticated()){
            throw new BadCredentialsException("Bad credentials");
        }
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
    }

    public void updateUsername(UserLoginDTO userLoginDTO, String username) throws BadCredentialsException {
        this.verify(userLoginDTO);
        User existingUser = this.getCurrentUser();
        existingUser.setUsername(username);
        userRepository.save(existingUser);
    }

    public void updateEmail(UserLoginDTO userLoginDTO, String email) throws BadCredentialsException {
        this.verify(userLoginDTO);
        User existingUser = this.getCurrentUser();
        existingUser.setEmail(email);
        userRepository.save(existingUser);
    }

    public void deleteUser() {
        userRepository.delete(this.getCurrentUser());
    }
}
