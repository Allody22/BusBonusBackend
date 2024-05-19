package ru.nsu.services.interfaces;


import ru.nsu.model.constants.DocumentTypes;
import ru.nsu.model.user.Account;

import java.util.Date;

public interface IUserDataService {

    /**
     * Обновление информации об аккаунте пользователя.
     * Все изменения сохраняются и их версионность записывается, в будущем человек сможет отследить порядок своих действий.
     * Если передаётся null или пустая строка, то данные очищаются в профиле, заполненность аккаунта становится меньше.
     * Если передаётся такая же информация, то ничего не меняется, заполненность аккаунта не меняется.
     * Если же передаются новые данные, а прошлые данные отсутствовали, то заполненность аккаунта увеличивается.
     *
     * @param name       имя пользователя.
     * @param lastName   фамилия пользователя.
     * @param patronymic отчество пользователя.
     * @param birthDate  дата рождения пользователя.
     * @param gender     гендер (пол) пользователя.
     * @param dataId     айди информации о пользователе, которое меняется. Необходимо так как может меняться и информация о попутчике.
     * @return сообщение, содержащее информации об обновлённых полях.
     */
    String updateUserData(String name, String lastName,
                          String patronymic, Date birthDate,
                          String gender, Long dataId);

    /**
     * Поиск типа документа из БД по его названию.
     *
     * @param type строковое название типа документа.
     * @return сущность типа документа из БД.
     */
    DocumentTypes findDocumentType(String type);

    /**
     * Добавляем в аккаунт нового попутчика со всей информацией.
     * Некоторая информация (документы) является обязательной.
     *
     * @param account                аккаунт, к которому привязывается попутчик.
     * @param gender                 гендер (пол) попутчика.
     * @param firstName              имя попутчика.
     * @param birthDate              дата рождения попутчика.
     * @param lastName               фамилия попутчика.
     * @param patronymic             отчество попутчика.
     * @param ticketCategoryString   строковая категория билета попутчика.
     * @param documentNumber         номер документа попутчика.
     * @param documentSeries         серия документа попутчика.
     * @param additionalData         дополнительная информация о документе, если такая имеется.
     * @param citizenship            гражданство попутчика.
     * @param issueDate              дата начала действия документа (выдачи).
     * @param issuingAuthority       организация, выдавшая документ.
     * @param expirationDate         дата конца действия документа.
     * @param confirmationOfBenefits документ, выдающий преимущество (студенческий).
     * @param documentType тип документа, привязанный к попутчику.
     * @return сообщение
     */
    String addCompanion(Account account, String gender, String firstName, Date birthDate,
                        String lastName, String patronymic, String ticketCategoryString,
                        String documentNumber, String documentSeries,
                        String additionalData, String citizenship,
                        Date issueDate, String issuingAuthority, Date expirationDate,
                        String confirmationOfBenefits, String documentType);

    /**
     * Удаляем попутчика по данным (UserData) из определённого аккаунта.
     *
     * @param name имя попутчика.
     * @param lastName фамилия попутчика.
     * @param patronymic отчество попутчика.
     * @param citizenship гражданство попутчика.
     * @param documentType тип документа попутчика.
     * @param documentNumber номер документа попутчика.
     * @param account аккаунт, из которого удаляется попутчик.
     * @param documentSeries серия документа попутчика.
     */
    void deleteCompanionsByDocumentsAndNameWithoutAccount(String name, String lastName, String patronymic,
                                                          String citizenship, DocumentTypes documentType, String documentNumber,
                                                          Account account, String documentSeries);
}
