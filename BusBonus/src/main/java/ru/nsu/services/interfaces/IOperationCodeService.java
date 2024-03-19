package ru.nsu.services.interfaces;


import ru.nsu.model.operations.CodeOperationDirection;
import ru.nsu.model.operations.OperationCodeNames;
import ru.nsu.model.operations.OperationPincode;

import java.util.List;

public interface IOperationCodeService {

    /**
     * Сохраняем новую операцию, связанную с пинкодом, по определённому логику.
     * Это действие не привязывается к аккаунту, потому что оно предназначено для
     * незарегистрированных пользователей и идёт проверка регистрации.
     *
     * @param login              - информация, которая используется для ассоциации кода с пользователем.
     * @param pinCodeData        - сам пинкод.
     * @param operationName      - название операции, то есть, того что происходит с пинкодом (формирование, соответствие или не соответствие).
     *                           Эти данные вносятся с помощью миграции.
     * @param operationDirection - направление операции, то есть для чего она нужна (регистрация, восстановление пароля и тп).
     *                           Эти данные тоже вносятся с помощью миграции.
     */
    void saveNewPinCodeOperation(String login, String pinCodeData,
                                 OperationCodeNames operationName, CodeOperationDirection operationDirection);

    /**
     * Поиск сущности операции с пинкодом по её строковому названию.
     * Это нужно для дальнейшей связи этой операции с какой-то сущностью в базе данных.
     *
     * @param operationName - имя операции (Формирования, соответствие или не соответствие)
     * @return - сущность операции.
     */
    OperationCodeNames findOperationName(String operationName);

    /**
     * Поиск сущности направления (задачи) операции по его строковому названию.
     *
     * @param operationDirection - строковое название задачи операции
     *                           Восстановление пароля, Регистрация аккаунта на сайте,
     *                           Смена телефона, Смена почты, Регистрация аккаунта.
     * @return - сущность направления операции.
     */
    CodeOperationDirection findOperationDirection(String operationDirection);

    /**
     * Находим список всех операций с пинкодами по определённому номеру телефона.
     * Пользователь не обязательно должен быть зарегистрирован, ведь мы ищем операции
     * по телефону, а не по аккаунту.
     *
     * @param phone - номер телефона
     * @return - список всех операций
     */
    List<OperationPincode> findAllAccountOperationsByPhone(String phone);

    /**
     * Ищем операцию определённого номера телефона, сделанную в последние minutesFromNow минут
     * по действия в этой операции (context) и по направлению самой операции (регистрация, смена почты и тп).
     * Этот метод необходим, например, чтобы найти последний код, который мы отправляли
     * пользователю для регистрации, а у этого кода есть определённое время жизни.
     *
     * @param phone              - номер телефона, к которому относятся операции.
     * @param minutesFromNow     - время, в которое надо найти операции.
     * @param context            - дополнительные условия операции, если такие есть.
     * @param operationDirection - название самой операции.
     * @return - сущность операции с пинкодом.
     */
    OperationPincode findLastCodeByPhoneAndDateAfter(String phone, int minutesFromNow, String context, String operationDirection);

    /**
     * Ищем операции определённого номера телефона, сделанную в последние minutesFromNow минут по названию операции
     * и по названию самой операции (регистрация, смена почты и тп).
     * Этот метод необходим, например, чтобы узнать, сколько раз пользователь запрашивал за
     * последнее время пинкоды, а допустимое кол-во пинкодов ограничено.
     *
     * @param phone          - номер телефона, к которому относятся операции.
     * @param minutesFromNow - время, в которое надо найти операции.
     * @param operationName  - дополнительные условия операции, если такие есть.
     * @param direction      - название самой операции.
     * @return - список сущностей операции с пинкодом.
     */
    List<OperationPincode> findAllByOperationNameDirectionAndDateAfter(String phone, int minutesFromNow, String operationName, String direction);

    /**
     * Ищем операции определённого логина, сделанную в последние minutesFromNow минут по названию операции
     * и по названию самой операции (регистрация, смена почты и тп).
     * Этот метод необходим, например, чтобы узнать, сколько раз пользователь запрашивал за
     * последнее время пинкоды, а допустимое кол-во пинкодов ограничено.
     * Метод похож на findAllByOperationNameDirectionAndDateAfter, но вместо номера телефона может
     * выступать любой логин (например почта или комбинация телефона+почта).
     *
     * @param phone          - номер телефона, к которому относятся операции.
     * @param minutesFromNow - время, в которое надо найти операции.
     * @param operationName  - дополнительные условия операции, если такие есть.
     * @param direction      - название самой операции.
     * @return - список сущностей операции с пинкодом.
     */
    List<OperationPincode> findAllByOperationNameDirectionAndDateAfterLikeUserLogin(String phone, int minutesFromNow, String operationName, String direction);

}
