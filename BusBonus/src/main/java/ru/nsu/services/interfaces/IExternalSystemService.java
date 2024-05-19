package ru.nsu.services.interfaces;

import ru.nsu.payload.response.CredentialsResponse;
import ru.nsu.payload.response.FullOrganizationInfoResponse;

import java.util.List;

public interface IExternalSystemService {

    /**
     * Проверка данных внешней системы, которая делает запрос.
     *
     * @param login    логин, привязанный к зарегистрированной в системе организации.
     * @param password пароль, выданной для данной организации.
     * @return true если такие данные существуют и они верные.
     */
    boolean validateDate(String login, String password);

    /**
     * Получение всех организаций, зарегистрированных в нашем сервисе.
     * Задумано как админский запрос, чтобы проверить данные.
     *
     * @return список с именами организаций, ИНН и данными.
     */
    List<FullOrganizationInfoResponse> getAllOrganizationData();

    /**
     * Регистрация нового аккаунта для организации.
     * То есть для уже существующей и зарегистрированный организации создаётся новый аккаунт (уникальный набор логин и пароль).
     * Проверяется что организация с таким названием или ИНН зарегистрирована, а потом генерируется случайный уникальный логин и пароль.
     *
     * @param organizationName зарегистрированное название организации.
     * @param organizationInn  зарегистрированный ИНН.
     * @return сгенерированные данные.
     */
    CredentialsResponse registerNewOrganization(String organizationName, String organizationInn);

    /**
     * Генерируется случайный уникальный логин, который будет привязываться к организации и
     * будет в комбинации с верным паролем давать доступ к запросам контроллера.
     *
     * @return сгенерированный уникальный логин.
     */
    String createUniqueCredentialsLogin();

}
