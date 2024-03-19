package ru.nsu.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.model.operations.CodeOperationDirection;
import ru.nsu.model.operations.OperationCodeNames;
import ru.nsu.model.operations.OperationPincode;
import ru.nsu.model.user.Account;
import ru.nsu.payload.request.LoginRequest;
import ru.nsu.payload.request.PasswordConfirmRequest;
import ru.nsu.payload.request.RegistrationRequest;
import ru.nsu.payload.response.BusBonusIDResponse;
import ru.nsu.payload.response.DataResponse;
import ru.nsu.payload.response.MessageResponse;
import ru.nsu.services.RefreshTokenService;
import ru.nsu.services.interfaces.IAccountService;
import ru.nsu.services.interfaces.IOperationAccountService;
import ru.nsu.services.interfaces.IOperationCodeService;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/site")
public class SiteV1Controller {

    private final IAccountService accountService;

    private final RefreshTokenService refreshTokenService;

    private final IOperationCodeService operationCodeService;

    private final IOperationAccountService operationAccountService;


    @PostMapping("/forgot")
    @Transactional
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody LoginRequest loginRequest) {
        String userPhone = loginRequest.getLogin();

        if (!accountService.checkAccExistenceByPhone(userPhone)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка введённых данных. Внимательно проверьте их еще раз для телефона " + userPhone));
        }

        List<OperationPincode> previousUserCodeOperations = operationCodeService.findAllByOperationNameDirectionAndDateAfter(userPhone, 10, "Формирование пин-кода", "Восстановление пароля");
        // Нельзя вызывать больше 3 смс в 10 минут
        if (previousUserCodeOperations.size() >= 3) {
            return ResponseEntity.badRequest().body(new MessageResponse("Превышено количество смс для смены пароля в час."));
        }

        String secretCode = accountService.generateRandomPassword();

        System.out.println("NEW SECRET CODE FOR PASSWORD CHANGE FOR " + userPhone + ":" + secretCode);

        CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Восстановление пароля");
        OperationCodeNames operationCodeName = operationCodeService.findOperationName("Формирование пин-кода");
        operationCodeService.saveNewPinCodeOperation(userPhone, secretCode, operationCodeName, operationDirection);

        return ResponseEntity.ok(new DataResponse(true));
    }

    @Operation(
            summary = "Подтверждение смены или восстановления пароля",
            description = "Отправка запроса с новым паролем и кодом подтверждения из смс для смены пароля. " +
                    "Код ищется за последние 10 минут с проверкой на то, что это код именно для смены пароля",
            tags = {"password", "post", "confirm"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Для пользователя установлен новый пароль, информация об этом записана в бд",
                    content = {@Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Превышено кол-во попыток ввода пинкода или ошибка в данных запроса", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/forgot/confirm")
    @Transactional
    public ResponseEntity<?> forgotPasswordConfirm(@Valid @RequestBody PasswordConfirmRequest passwordConfirmRequest) {
        String userPhone = passwordConfirmRequest.getLogin();
        String newPassword = passwordConfirmRequest.getPassword();
        String userCode = passwordConfirmRequest.getCodeToken();

        List<OperationPincode> previousUserCodeOperations = operationCodeService.findAllByOperationNameDirectionAndDateAfter(userPhone, 10, "Пин-код не соответствует", "Восстановление пароля");

        if (previousUserCodeOperations.size() >= 6) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Вы использовали слишком много попыток ввода кода за последние 10 минут"));
        }

        //Достаём последний пинкод за 10 минут
        var latestCode = operationCodeService.findLastCodeByPhoneAndDateAfter(userPhone, 10, "Формирование пин-кода", "Восстановление пароля");
        String confirmationCodeFromDB = latestCode.getPinCodeData();

        if (!(userCode.equals(confirmationCodeFromDB))) {
            //Сохраняем что пользователь ввёл неверный код
            CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Восстановление пароля");
            OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код не соответствует");
            operationCodeService.saveNewPinCodeOperation(userPhone, userCode, operationCodeName, operationDirection);
            //Код подтверждения не совпадает
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка введённых данных. Внимательно проверьте их еще раз для телефона " + userPhone));
        } else {
            Account accountByPhone = accountService.getAccountByPhone(userPhone);
            if (accountByPhone != null) {
                accountService.updateAccountPassword(accountByPhone.getId(), newPassword);

                CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Восстановление пароля");
                OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код соответствует");
                operationCodeService.saveNewPinCodeOperation(userPhone, userCode, operationCodeName, operationDirection);

                operationAccountService.saveNewAccountOperation(accountByPhone, "Пользователь восстанавливает пароль",
                        "Аккаунт подтвердил смену пароля");
                return ResponseEntity.ok(new DataResponse(true));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Ошибка введённых данных. Внимательно проверьте их еще раз для телефона " + userPhone));
            }
        }
    }

    @PostMapping("/registration")
    @Transactional
    public ResponseEntity<?> registration(@Valid @RequestBody LoginRequest loginRequest) {
        String phone = loginRequest.getLogin();
        List<OperationPincode> previousUserCodeOperations = operationCodeService.findAllByOperationNameDirectionAndDateAfter(phone, 10, "Формирование пин-кода", "Регистрация аккаунта");

        // Нельзя вызывать больше 2 смс в 10 минут
        if (previousUserCodeOperations.size() >= 3) {
            return ResponseEntity.badRequest().body(new MessageResponse("Превышено количество смс для регистрации в час."));
        }

        // Проверяем не существует ли уже такой акк
        if (accountService.checkAccExistenceByPhone(phone)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Телефон " + phone + " уже зарегистрирован."));
        }

        String accountCode = accountService.generateRandomPassword();

        //Высылаем этот пароль в смс но пока тупо в логах

        System.out.println("NEW REGISTRATION CODE FOR " + phone + ":" + accountCode);

        //Сохраняем действия в таблице операции с кодом
        CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Регистрация аккаунта");
        OperationCodeNames operationCodeName = operationCodeService.findOperationName("Формирование пин-кода");
        operationCodeService.saveNewPinCodeOperation(phone, accountCode, operationCodeName, operationDirection);

        return ResponseEntity.ok(new DataResponse(true));
    }


    @Operation(
            summary = "Подтверждение регистрации с сайта",
            description = "Отправка запроса с кодом подтверждения из смс, паролем и телефоном для подтверждения этого номера. " +
                    "Код ищется за последние 10 минут с проверкой на то, что это код именно для регистрации",
            tags = {"phone", "post", "confirm", "account"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Аккаунт становится активным, информация об этом записана в бд. Аккаунты открывается доступ ко всему функционалу сайта",
                    content = {@Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса или превышено количество попыток ввода пинкода (больше 6 за последние 10 минут)", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/registration/confirm")
    @Transactional
    public ResponseEntity<?> registrationConfirm(@Valid @RequestBody RegistrationRequest registrationRequest) {
        String userPhone = registrationRequest.getLogin();
        String userCode = registrationRequest.getCodeToken();

        List<OperationPincode> previousUserCodeOperations = operationCodeService.findAllByOperationNameDirectionAndDateAfter(userPhone, 10, "Пин-код не соответствует", "Регистрация аккаунта");

        if (previousUserCodeOperations.size() >= 6) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Вы использовали слишком много попыток ввода кода за последние 10 минут"));
        }

        //Достаём последний пинкод за 10 минут
        var latestCode = operationCodeService.findLastCodeByPhoneAndDateAfter(userPhone, 10, "Формирование пин-кода", "Регистрация аккаунта");

        String confirmationCodeFromDB = latestCode.getPinCodeData();
        if (!(userCode.equals(confirmationCodeFromDB))) {
            //Сохраняем что пользователь ввёл неверный код
            CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Регистрация аккаунта");
            OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код не соответствует");
            operationCodeService.saveNewPinCodeOperation(userPhone, userCode, operationCodeName, operationDirection);

            //Код подтверждения не совпадает
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка введённых данных. Внимательно проверьте их еще раз для телефона " + userPhone));
        } else {
            Account accountByPhone = accountService.createNewAccountFromSite(userPhone, registrationRequest.getPassword());
            //В таблице операций личного кабинета пишем что чел ввёл правильный пинкод для регистрации
            CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Регистрация аккаунта");
            OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код соответствует");
            operationCodeService.saveNewPinCodeOperation(userPhone, userCode, operationCodeName, operationDirection);

            operationAccountService.saveNewAccountOperation(accountByPhone, "Пользователь успешно регистрируется с сайта",
                    "Пользователь зарегистрировался по телефону " + userPhone);

            String busBonusId = accountByPhone.getBusBonusId();

            return ResponseEntity.ok(new BusBonusIDResponse(busBonusId));
        }
    }
}
