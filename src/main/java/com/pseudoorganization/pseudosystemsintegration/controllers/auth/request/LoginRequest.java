package com.pseudoorganization.pseudosystemsintegration.controllers.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    String username;
    String password;
}
