package ru.nsu.services.interfaces;


import com.nimbusds.jose.util.Pair;

public interface ISmsService {

    /**
     * Метод для отправки смс с кодом для восстановления пароля аккаунта.
     *
     * @param codeToken   секретный код, отправляемый на номер телефона.
     * @param phoneNumber номер телефона, на который надо отправить токен.
     * @return информация об ответе от смс оператор (тело запроса и статус запроса).
     */
    Pair<String, Boolean> forgotPasswordSms(String codeToken, String phoneNumber);

    /**
     * Отправляем смс при регистрации для подтверждения номера телефона.
     *
     * @param codeToken   секретный код, сгенерированный на сервере.
     * @param phoneNumber номер телефона, который пытается зарегистрироваться.
     * @return информация об ответе от смс оператор (тело запроса и статус запроса).
     */
    Pair<String, Boolean> sendRegistrationSms(String codeToken, String phoneNumber);

    /**
     * Отправка на номер телефона смс об успешном подтверждении номера телефона.
     *
     * @param phoneNumber новый зарегистрированный номер телефона.
     * @param busBonusId  сгенерированный уникальный busBonusId для данного аккаунта.
     * @return информация об ответе от смс оператор (тело запроса и статус запроса).
     */
    Pair<String, Boolean> confirmRegistration(String phoneNumber, String busBonusId);
}