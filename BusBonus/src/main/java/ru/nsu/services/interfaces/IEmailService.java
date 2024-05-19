package ru.nsu.services.interfaces;

public interface IEmailService {

    /**
     * Отправляет на почту письмо с секретным кодом для подтверждения почты.
     *
     * @param to         - на какую почту отправить письмо
     * @param secretCode - секретный код
     * @throws Exception - ошибка если передан неправильный формат почты
     */
    void sendRegistrationMessage(String to, String secretCode) throws Exception;

    /**
     * Отправления письма, подтверждающего почту.
     *
     * @param to - адрес на который надо отправить письма
     * @throws Exception - ошибка если передан неправильный формат почты
     */
    void sendConfirmationMessage(String to) throws Exception;
}
