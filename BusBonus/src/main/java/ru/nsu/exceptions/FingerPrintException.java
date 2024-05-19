package ru.nsu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class FingerPrintException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public FingerPrintException(String fingerPrint, String message) {
        super(String.format("Ошибка для фингерпринта [%s]: %s", fingerPrint, message));
    }
}
