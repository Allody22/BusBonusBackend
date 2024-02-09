package ru.nsu.model.constants;

import lombok.ToString;

@ToString
public enum ERole {
    ROLE_USER, //Подтверждённый аккаунт
    ROLE_COMPANION, //Попутчик
    ROLE_MODERATOR,
    ROLE_ADMIN,
    ROLE_ADMINISTRATOR,
    ROLE_SPECIALIST
}
