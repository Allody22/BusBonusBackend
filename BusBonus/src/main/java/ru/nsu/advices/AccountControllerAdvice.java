package ru.nsu.advices;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nsu.exceptions.AccountActiveAndAlreadyExistException;
import ru.nsu.exceptions.AccountNotActiveAndAlreadyExistException;
import ru.nsu.exceptions.AccountNotFoundException;
import ru.nsu.exceptions.NotInDataBaseException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccountControllerAdvice {

    @ExceptionHandler(value = AccountNotActiveAndAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAccountNotActiveAndAlreadyExistException(AccountNotActiveAndAlreadyExistException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }


    @ExceptionHandler(value = NotInDataBaseException.class)
    @ResponseStatus(HttpStatus.GONE)
    public Map<String, String> handleNotInDataBaseException(NotInDataBaseException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFoundException(AccountNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(value = AccountActiveAndAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleUserAlreadyExistException(AccountActiveAndAlreadyExistException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }
}
