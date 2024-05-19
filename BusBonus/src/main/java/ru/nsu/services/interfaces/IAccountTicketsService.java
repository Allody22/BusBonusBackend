package ru.nsu.services.interfaces;

import ru.nsu.model.user.Account;
import ru.nsu.payload.request.NewTicketsRequest;
import ru.nsu.payload.response.AccountOrdersByStatusesResponse;

public interface IAccountTicketsService {

    /**
     * Получение всех типов заказов пользователя по айди его аккаунта.
     * Возвращается специальная схема, где идут запланированные и совершённые заказы человека.
     *
     * @param accountId айди аккаунта в нашей системе.
     * @return схема с информацией о билетах
     */
    AccountOrdersByStatusesResponse getUserOrdersForResponseById(Long accountId);


    /**
     * Сохранение нового заказа (order) пользователя со всей информацией.
     * Создаётся набор билетов, привязанный к заказу и поездка привязанная к заказу.
     *
     * @param account           передаётся аккаунт пользователя. Он необходим, чтобы удалить кэш его поездок по его айди.
     * @param newTicketsRequest класс со всей информацией о заказе
     */
    void saveNewUserTicketsFromExternalSystem(Account account, NewTicketsRequest newTicketsRequest);
}
