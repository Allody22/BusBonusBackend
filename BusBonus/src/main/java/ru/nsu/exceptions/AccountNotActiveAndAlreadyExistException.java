package ru.nsu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountNotActiveAndAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AccountNotActiveAndAlreadyExistException() {
        super("Данный аккаунт уже зарегистрирован, но не подтверждён");
    }
}
