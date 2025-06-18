package com.sermaluc.prueba.apiuser.exception;

public class UserRegistrationException extends RuntimeException  {

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

}
