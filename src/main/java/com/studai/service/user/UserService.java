package com.studai.service.user;

import com.studai.domain.user.StudaiUserDetails;
import com.studai.domain.user.User;
import com.studai.domain.user.dto.UpdateUserDTO;
import com.studai.domain.user.dto.UserDTO;
import com.studai.domain.user.dto.UserLoginDTO;
import com.studai.domain.user.dto.UserRegisterDTO;
import com.studai.repository.user.UserRepository;
import com.studai.service.jwt.JWTService;
import com.studai.utils.assembler.UserAssembler;
import com.studai.utils.exception.UserAlreadyExistsException;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserAssembler userAssembler;

    public UserService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            JWTService jwtService,
            UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
        this.userAssembler = userAssembler;
    }

    public UserDTO register(UserRegisterDTO userDTO){

        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new UserAlreadyExistsException("User already exists with username: " + userDTO.getUsername());
        }

        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException("User already exists with email: " + userDTO.getEmail());
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User entity = userAssembler.toEntity(userDTO);

        return userAssembler.toDto(userRepository.save(entity));
    }

    public String verify(UserLoginDTO loginDTO) throws BadCredentialsException {

        User user = userRepository.findByUsername(loginDTO.getLogin());
        if(user == null) user = userRepository.findByEmail(loginDTO.getLogin());

        if(user == null) {
            throw new BadCredentialsException("Bad credentials");
        }

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

    public void updateCredentials(UpdateUserDTO updateUserDTO) throws BadCredentialsException {
        User existingUser = this.getCurrentUser();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(existingUser.getUsername(), updateUserDTO.getOldPassword()));
        if(!authentication.isAuthenticated()){
            throw new BadCredentialsException("Bad credentials");
        }

        if (StringUtils.isNotBlank(updateUserDTO.getUsername())){
            if(userRepository.findByUsername(updateUserDTO.getUsername()) != null) {
                throw new UserAlreadyExistsException("User already exists with username: " + updateUserDTO.getUsername());
            }
            existingUser.setUsername(updateUserDTO.getUsername());
        }

        if (StringUtils.isNotBlank(updateUserDTO.getEmail())){
            if(userRepository.findByEmail(updateUserDTO.getEmail()) != null) {
                throw new UserAlreadyExistsException("User already exists with email: " + updateUserDTO.getEmail());
            }
            existingUser.setEmail(updateUserDTO.getEmail());
        }

        if (StringUtils.isNotBlank(updateUserDTO.getNewPassword())){
            existingUser.setPassword(passwordEncoder.encode(updateUserDTO.getNewPassword()));
        }

        userRepository.save(existingUser);
    }

    public void deleteUser(String userPassword) throws BadCredentialsException {
        User existingUser = this.getCurrentUser();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(existingUser.getUsername(), userPassword));
        if(!authentication.isAuthenticated()){
            throw new BadCredentialsException("Bad credentials");
        }
        userRepository.delete(this.getCurrentUser());
    }
}
