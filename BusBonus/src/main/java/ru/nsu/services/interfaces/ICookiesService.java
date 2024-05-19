package ru.nsu.services.interfaces;

import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletRequest;

public interface ICookiesService {

    /**
     * Удаление cookies с определённым именем.
     * Возвращает кук, время жизни которого равно 0, то есть он сразу удалится на сайте.
     *
     * @param cookieName имя соответствующего кука.
     * @return сущность кука для запроса.
     */
    ResponseCookie deleteCookie(String cookieName);


    /**
     * Создание специального кука с определёнными параметрами для возврата из запроса.
     *
     * @param cookieName имя кука.
     * @param path       путь, куда этот кук будет отправляться.
     * @param value      значение соответствующего кука.
     * @param duration   время жизни кука на сайте.
     * @return сущности кука.
     */
    ResponseCookie generateCookie(String cookieName, String path, String value, long duration);

    /**
     * Получение значения параметра из кука с определённым именем.
     *
     * @param request    запрос из которого надо достать значение кука.
     * @param cookieName имя кука, которое нам надо достать.
     * @return значение кука.
     */
    String getCookieValueByName(HttpServletRequest request, String cookieName);
}
