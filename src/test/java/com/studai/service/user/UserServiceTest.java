package com.studai.service.user;

import com.studai.domain.user.StudaiUserDetails;
import com.studai.domain.user.User;
import com.studai.domain.user.dto.UserLoginDTO;
import com.studai.domain.user.dto.UserRegisterDTO;
import com.studai.repository.user.UserRepository;
import com.studai.service.jwt.JWTService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should register a user successfully when everything is OK")
    void registerUserCase1() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("user1", "pass1", "email1@example.com");
        when(passwordEncoder.encode("pass1")).thenReturn("encodedPass");

        User expectedUser = User.builder()
            .username("user1")
            .password("encodedPass")
            .email("email1@example.com")
            .build();
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        // Act
        User registeredUser = userService.register(userRegisterDTO);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("user1", registeredUser.getUsername());
        assertEquals("encodedPass", registeredUser.getPassword());
        assertEquals("email1@example.com", registeredUser.getEmail());
    }

    @Test
    @DisplayName("Should verify user credentials and return a token")
    void verifyUserSuccess() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO("user1", "pass1");

        // Create a dummy authentication that is authenticated.
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);
        when(jwtService.generateToken("user1")).thenReturn("token123");

        // Act
        String token = userService.verify(loginDTO);

        // Assert
        assertEquals("token123", token);
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when credentials are invalid in verify")
    void verifyUserFailure() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO("user1", "wrongPass");

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> userService.verify(loginDTO));
    }

    @Test
    @DisplayName("Should get the current user from the security context")
    void getCurrentUserSuccess() {
        // Arrange
        // Create a dummy StudaiUserDetails with the username "user1"
        StudaiUserDetails dummyDetails = mock(StudaiUserDetails.class);
        when(dummyDetails.getUsername()).thenReturn("user1");

        // Set up a dummy authentication with the dummy details
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(dummyDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Stub the repository to return a user when findByUsername is called
        User expectedUser = new User();
        expectedUser.setUsername("user1");
        expectedUser.setPassword("encodedPass");
        expectedUser.setEmail("email1@example.com");
        when(userRepository.findByUsername("user1")).thenReturn(expectedUser);

        // Act
        User currentUser = userService.getCurrentUser();

        // Assert
        assertNotNull(currentUser);
        assertEquals("user1", currentUser.getUsername());
    }

    @Test
    @DisplayName("Should update user's password successfully")
    void updatePasswordSuccess() {

        // Set up the security context
        StudaiUserDetails dummyDetails = mock(StudaiUserDetails.class);
        when(dummyDetails.getUsername()).thenReturn("user1");
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(dummyDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Stub the authentication manager so that verify() passes.
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);

        // Stub repository call for getCurrentUser()
        User existingUser = new User();
        existingUser.setUsername("user1");
        existingUser.setPassword(passwordEncoder.encode("oldEncodedPass"));
        existingUser.setEmail("email1@example.com");
        when(userRepository.findByUsername("user1")).thenReturn(existingUser);

        // Stub the password encoder for the new password.
        when(passwordEncoder.encode("newPass")).thenReturn("newEncodedPass");

        // Act
        userService.updatePassword("oldEncodedPass", "newPass");

        // Assert: Capture the User passed to userRepository.save and verify the password.
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("newEncodedPass", savedUser.getPassword());
    }


    @Test
    @DisplayName("Should throw BadCredentialsException when updating password with invalid credentials")
    void updatePasswordFailure() {

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad Credentials"));

    }

    @Test
    @DisplayName("Should update user's username successfully")
    void updateUsernameSuccess() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO("user1", "pass1");

        StudaiUserDetails dummyDetails = mock(StudaiUserDetails.class);
        when(dummyDetails.getUsername()).thenReturn("user1");
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(dummyDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Stub verify() to succeed
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);

        // Stub repository for getCurrentUser()
        User existingUser = new User();
        existingUser.setUsername("user1");
        existingUser.setPassword("encodedPass");
        existingUser.setEmail("email1@example.com");
        when(userRepository.findByUsername("user1")).thenReturn(existingUser);

        // Act
        userService.updateUsername(loginDTO, "newUsername");

        // Assert: Verify that the saved user has the new username.
        verify(userRepository).save(argThat(user -> "newUsername".equals(user.getUsername())));
    }

    @Test
    @DisplayName("Should update user's email successfully")
    void updateEmailSuccess() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO("user1", "pass1");

        StudaiUserDetails dummyDetails = mock(StudaiUserDetails.class);
        when(dummyDetails.getUsername()).thenReturn("user1");
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(dummyDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Stub verify() to succeed.
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);

        // Stub repository for getCurrentUser()
        User existingUser = new User();
        existingUser.setUsername("user1");
        existingUser.setPassword("encodedPass");
        existingUser.setEmail("email1@example.com");
        when(userRepository.findByUsername("user1")).thenReturn(existingUser);

        // Act
        userService.updateEmail(loginDTO, "newEmail@example.com");

        // Assert: Verify that the saved user has the new email.
        verify(userRepository).save(argThat(user -> "newEmail@example.com".equals(user.getEmail())));
    }

    @Test
    @DisplayName("Should delete current user successfully")
    void deleteUserSuccess() {
        // Arrange
        StudaiUserDetails dummyDetails = mock(StudaiUserDetails.class);
        when(dummyDetails.getUsername()).thenReturn("user1");
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(dummyDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Stub repository for getCurrentUser()
        User existingUser = new User();
        existingUser.setUsername("user1");
        existingUser.setPassword("encodedPass");
        existingUser.setEmail("email1@example.com");
        when(userRepository.findByUsername("user1")).thenReturn(existingUser);

        // Act
        userService.deleteUser();

        // Assert: Verify that the repository's delete method was called with the expected user.
        verify(userRepository).delete(existingUser);
    }
}
