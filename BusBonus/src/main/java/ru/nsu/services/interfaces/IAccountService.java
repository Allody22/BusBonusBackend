package ru.nsu.services.interfaces;

import ru.nsu.model.user.Account;
import ru.nsu.model.user.UserData;

import java.util.Date;
import java.util.List;

public interface IAccountService {

    /**
     * Получение всех типов документов, доступных для ввода на сайте.
     *
     * @return - список из названий документов
     */
    List<String> getAllDocumentTypes();

    /**
     * Получение всех категорий билетов, доступных при покупке билетов через сайт.
     *
     * @return - список из категорий.
     */
    List<String> getAllTicketCategories();

    /**
     * Смена пароля у аккаунта через смс.
     *
     * @param accountId - айди аккаунта в базе данных, желающего сменить пароль.
     * @param password  - новый пароль аккаунта.
     */
    void updateAccountPassword(Long accountId, String password);

    /**
     * Смена пароля с актуального на новый при правильном вводе актуального пароля.
     *
     * @param account     - аккаунт желающий сменить пароль.
     * @param newPassword - новый пароль.
     * @param oldPassword - старый пароль.
     * @return - возвращается true, если переданный oldPassword совпадает с актуальным. Иначе false.
     */
    boolean updateAccountPasswordFromPrevious(Account account, String newPassword, String oldPassword);

    /**
     * Проверка существования аккаунта по телефону.
     *
     * @param phone - телефон, интересующего аккаунта.
     * @return - true, если аккаунт существует, а иначе false.
     */
    boolean checkAccExistenceByPhone(String phone);

    /**
     * Получение аккаунта по номеру телефона.
     *
     * @param phone - номер телефона.
     * @return - аккаунт, если такой существует, иначе null.
     */
    Account getAccountByPhone(String phone);

    /**
     * Получение аккаунта по его айди в базе данных.
     *
     * @param accountId - айди аккаунта в базе данных
     * @return - аккаунт, если такой существует, иначе null.
     */
    Account getAccountById(Long accountId);

    /**
     * Функция ищет глава аккаунта и его данные по информации о самом аккаунте.
     *
     * @param account - аккаунт человека.
     * @return - сущности информации о пользователе, являющемся главой аккаунте, иначе ошибка.
     */
    UserData getOwnerUserData(Account account);

    /**
     * Обновление документов пользователя по идентификатору пользователя.
     * Позволяет обновить информацию о документах пользователя, включая тип документа, номер, серию,
     * орган выдачи, дату выдачи, срок действия, дополнительные данные, гражданство,
     * подтверждение льгот и категорию билета.
     *
     * @param userDataId             - идентификатор информации о пользователе.
     * @param documentType           - тип документа.
     * @param documentNumber         - номер документа.
     * @param documentSeries         - серия документа.
     * @param issuingAuthority       - орган, выдавший документ.
     * @param issueDate              - дата выдачи документа.
     * @param expirationDate         - срок действия документа.
     * @param additionalData         - дополнительные данные, связанные с документом.
     * @param citizenship            - гражданство, указанное в документе.
     * @param confirmationOfBenefits - подтверждение льгот, если применимо.
     * @param ticketCategory         - категория билета, связанная с пользователем.
     * @return - строка, описывающая все обновления, то есть новые переданные данные.
     */
    String updateUserDocumentByUserId(Long userDataId, String documentType, String documentNumber, String documentSeries,
                                      String issuingAuthority, Date issueDate, Date expirationDate, String additionalData,
                                      String citizenship, String confirmationOfBenefits, String ticketCategory);

    /**
     * Обновление номера телефона и электронной почты аккаунта по его идентификатору.
     * Позволяет изменить контактные данные аккаунта, включая номер телефона и электронную почту.
     *
     * @param accountId - идентификатор аккаунта в базе данных, для которого требуется обновление.
     * @param phone     - новый номер телефона аккаунта.
     * @param email     - новая электронная почта аккаунта.
     */
    void updateAccountPhoneAndEmailByAccountId(Long accountId, String phone, String email);

    /**
     * Обновляет почту в аккаунте по его айди.
     *
     * @param accountId - айди аккаунта.
     * @param email     - новая устанавливаемая почта
     */
    void updateAccountEmailByAccountId(Long accountId, String email);

    /**
     * Проверка существования аккаунта и состояния его активности.
     *
     * @param phone - номер телефона аккаунта.
     * @return true, если аккаунт активен и подтверждён, иначе false.
     */
    boolean checkAccStatus(String phone);


    /**
     * Функция создания нового аккаунта с параллельной генерацией случайного уникального BBId.
     *
     * @param phone - телефон на который хотят зарегистрировать аккаунт.
     * @param password - пароль нового аккаунта.
     * @return - сама сущность нового аккаунта
     */
    Account createNewAccountFromSite(String phone, String password);
}
