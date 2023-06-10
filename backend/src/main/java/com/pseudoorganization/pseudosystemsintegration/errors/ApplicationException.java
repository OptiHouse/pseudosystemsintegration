package com.pseudoorganization.pseudosystemsintegration.errors;

import lombok.Getter;

@Getter
public class ApplicationException extends Exception{

    private final ErrorCodes errorCode;
    public ApplicationException(ErrorCodes errorCode) {
        super(errorCode.message);
        this.errorCode = errorCode;
    }

}
