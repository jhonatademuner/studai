package com.studai.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studai.domain.user.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterDTO {

    private String username;
    private String password;
    private String email;
    private UserRole role;

}
