package ru.nsu.services.interfaces;


import ru.nsu.model.operations.OperationAccount;
import ru.nsu.model.user.Account;

import java.util.List;

public interface IOperationAccountService {

    /**
     * Сохраняем операцию, связанную с изменением в аккаунте и привязываем к аккаунту.
     * Это может быть как внесение данных, так и обнуление каких-то данных.
     * Мы стараемся отслеживать всё.
     *
     * @param userAccount - аккаунт пользователя.
     * @param methodName  - название метод/операции. Эти данные вносятся миграцией, как константные значения, и их много.
     *                    Смена номера телефона, почты, документов, добавление попутчика, удаление попутчика, регистрация,
     *                    сменя пароля, добавление автобусного билета, смена информации пользователя о себе (ФИО, др, гендер).
     * @param description - конкретное описание действия в аккаунте. Например: "Владелец аккаунт сменил свою почту на 'helloWorld@gmail.com'.
     */
    void saveNewAccountOperation(Account userAccount, String methodName, String description);

    /**
     * Получение всех операций изменения внутри аккаунта. Сейчас это админский запрос, которые нужен,
     * чтобы отследить подозрительную активность внутри аккаунта, но в будущем у пользователя тоже
     * будет возможность посмотреть историю своих изменений.
     *
     * @param phone - номер телефона, для которого мы ищем операции.
     * @return - список всех операций
     */
    List<OperationAccount> getAllAccountOperationByPhone(String phone);

}
