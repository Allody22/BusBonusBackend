package ru.nsu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountActiveAndAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AccountActiveAndAlreadyExistException() {
        super("Такой аккаунт уже существует и активирован");
    }
}
