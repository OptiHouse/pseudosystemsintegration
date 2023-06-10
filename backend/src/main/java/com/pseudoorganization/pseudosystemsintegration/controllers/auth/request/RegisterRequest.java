package com.pseudoorganization.pseudosystemsintegration.controllers.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    String username;
    String password;
}
