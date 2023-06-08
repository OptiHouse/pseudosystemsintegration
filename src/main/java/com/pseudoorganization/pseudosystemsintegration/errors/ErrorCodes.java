package com.pseudoorganization.pseudosystemsintegration.errors;

public enum ErrorCodes {
    USER_ALREADY_EXISTS("Użytkownik już istnieje"),
    USER_NOT_FOUND("Użytkownik nie istnieje"),
    USER_NOT_AUTHORIZED("Użytkownik nie jest autoryzowany"),
    USER_NOT_LOGGED_IN("Użytkownik nie jest zalogowany"),
    USER_NOT_LOGGED_OUT("Użytkownik nie jest wylogowany"),
    USER_NOT_ADMIN("Użytkownik nie jest administratorem");

    final String message;

    ErrorCodes(String message) {
        this.message = message;
    }
}
