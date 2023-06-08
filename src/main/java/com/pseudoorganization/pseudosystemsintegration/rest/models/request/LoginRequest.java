package com.pseudoorganization.pseudosystemsintegration.rest.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    String username;
    String password;
}
