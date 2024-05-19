package ru.nsu.services.interfaces;

import org.springframework.http.ResponseCookie;
import ru.nsu.model.RefreshToken;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IRefreshTokenService {

    /**
     * Поиск списка всех рефреш токенов, привязанных к айди аккаунта.
     *
     * @param accountId интересующий нас айди аккаунт.
     * @return список из всех токенов.
     */
    List<RefreshToken> findAllByAccountId(Long accountId);

    /**
     * Метод для выхода из аккаунта транзакционного (когда человек передаёт правильные данные).
     *
     * @param request запрос с информацией о рефреш токене в хедерах.
     */
    void processLogout(HttpServletRequest request);

    /**
     * Создание рефреш токена, привязанного к аккаунту пользователя с определённым фингерпринтом,
     * то есть по факту создание сессии пользователя в определённом браузере.
     *
     * @param accountId   айди аккаунта пользователя.
     * @param fingerPrint фингерпринт переданного браузера пользователя.
     * @return созданная сущность токена.
     */
    RefreshToken createRefreshToken(Long accountId, String fingerPrint);

    /**
     * Ищем из базы данных рефреш токен, по переданному ему строковому представлению токена.
     *
     * @param refreshToken строковое представление самого токена.
     * @return сущность токена из БД.
     */
    RefreshToken findByRefreshToken(String refreshToken);

    /**
     * Удаляем куку, связанную с данным токеном, то есть возвращаем куку
     * с соответствующим названием, но временем жизни 0.
     *
     * @return сущность cookies для возврата из запроса с информацией о токене.
     */
    ResponseCookie deleteRefreshJwtCookie();

    /**
     * Удаляем все рефреш токены, связанные с аккаунтом по его айди.
     *
     * @param accountId айда аккаунта, с которым связаны токены.
     * @return пустая кука с нулевым временем жизни.
     */
    ResponseCookie deleteAllRefreshJwtCookieByAccountId(Long accountId);

    /**
     * Удаляем все токены, привязанные к аккаунту пользователя с определённым фингерпринтом.
     * То есть если человек в рамках одной сессии смог создать несколько токенов (одно сессия определяется в зависимости от fingerprint),
     * то мы удаляем их всех.
     *
     * @param fingerPrint фингерпринт аккаунта.
     * @param accountId   айди аккаунта.
     * @return true в случае успешного удаления.
     */
    boolean deleteAllRefreshTokenByFingerPrintAndAccountId(String fingerPrint, Long accountId);

    /**
     * Удаление всех рефреш токенов в аккаунте.
     *
     * @param accountId айди аккаунта
     */
    void deleteAllRefreshTokenByAccountId(Long accountId);

    /**
     * Создаём куку для рефреш токена со временем жизни,
     * равным времени жизни самого токена с путями прописанными для обновления токена.
     *
     * @param refreshToken строковое представление токена.
     * @return сущность cookies для возврата из запроса с информацией о токене.
     */
    ResponseCookie generateRefreshJwtCookie(String refreshToken);

    /**
     * Пытаемся достать из переданного нам хедера с cookies строковое представление самого рефреш токена.
     *
     * @param request переданный нам запрос.
     * @return строковое представление токена.
     */
    String getJwtRefreshFromCookies(HttpServletRequest request);

    /**
     * Ищем токен по его строковому представлению.
     * Сначала пытайся найти его кеш в Redis, а затем обращаемся к БД.
     *
     * @param token строковое представление токена.
     * @return сущность рефреш токена из БД или кеша.
     */
    RefreshToken findByTokenInCache(String token);

    /**
     * Удаляем рефреш токен из кэша и из базы данных.
     *
     * @param refreshToken строковое представление токена.
     */
    void deleteRefreshToken(String refreshToken);

    /**
     * Проверяем актуален ли рефреш токен, не прошло ли еще его время жизни.
     * Если его время жизни прошло, то удаляем токен и выбрасываем ошибку.
     *
     * @param token интересующая нас сущность токена.
     * @return сам токен, если всё хорошо
     */
    RefreshToken verifyExpiration(RefreshToken token);
}
