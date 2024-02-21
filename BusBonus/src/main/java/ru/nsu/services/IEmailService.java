package ru.nsu.services;

public interface IEmailService {

    /**
     * Отправляет на почту письмо с секретным кодом для подтверждения почты.
     *
     * @param to - на какую почту отправить письмо
     * @param secretCode - секретный код
     * @throws Exception - ошибка если передан неправильный формат почты
     */
    void sendRegistrationMessage(String to, String secretCode) throws Exception;

    void sendConfirmationMessage(String to);
}
