package ru.nsu.services.interfaces;

public interface IValidationClass {

    /**
     * Валидация ФИО человека, который пытается внести эти данные у нас на сайте.
     * Валидации идёт если передаваемое поле не null и не пустое, потому что пользователь может
     * хотеть удалить эти данные из профиля.
     * Поля должны содержать русские или английские буквы длинною до 64 символов.
     *
     * @param firstName имя пользователя.
     * @param lastName фамилия пользователя.
     * @param patronymic отчество человека.
     */
    void validUserFullName(String firstName, String lastName, String patronymic);

    /**
     * Валидация документов по шаблонам из официальных законов/документов РФ.
     *
     * @param docTypeCode код документа (например для паспорта 21).
     * @param docSeries серия документа по шаблону, подходящему ему.
     * @param docNum номер документа по шаблону, подходящему ему.
     * @param citizenship гражданство человека.
     */
    void validateDocument(int docTypeCode, String docSeries, String docNum, String citizenship);
}
