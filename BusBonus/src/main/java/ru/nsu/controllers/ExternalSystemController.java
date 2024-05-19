package ru.nsu.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.model.operations.CodeOperationDirection;
import ru.nsu.model.operations.OperationCodeNames;
import ru.nsu.model.user.Account;
import ru.nsu.payload.request.LoginAndSecretCodeRequest;
import ru.nsu.payload.request.NewTicketsRequest;
import ru.nsu.payload.response.BusBonusIDResponse;
import ru.nsu.payload.response.DataResponse;
import ru.nsu.payload.response.MessageResponse;
import ru.nsu.services.interfaces.IAccountService;
import ru.nsu.services.interfaces.IAccountTicketsService;
import ru.nsu.services.interfaces.IOperationCodeService;
import ru.nsu.services.interfaces.ISmsService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/external")
public class ExternalSystemController {

    private final ISmsService smsService;

    private final IAccountService accountService;

    private final IOperationCodeService operationCodeService;

    private final IExternalSystemServic externalSystemServices;

    private final IAccountTicketsService accountTicketsService;

    @PostMapping("/check/phone/{phoneNumber}")
    @Transactional
    public ResponseEntity<?> checkPhone(
            @RequestHeader("login") String login,
            @RequestHeader("password") String password,
            @PathVariable("phoneNumber") @NotNull @NotBlank String phoneNumber) {

        if (!externalSystemServices.validateDate(login, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Неверные данные"));
        }

        String accountCode = accountService.generateRandomPassword();
        var smsResponse = smsService.sendRegistrationSms(accountCode, phoneNumber);
        if (!smsResponse.getRight()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка при отправке сообщения. Попробуйте позже или обратитесь в поддержку"));
        }

        CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Подтверждение телефона");
        OperationCodeNames operationCodeName = operationCodeService.findOperationName("Формирование пин-кода");
        operationCodeService.saveNewPinCodeOperation(phoneNumber, accountCode, operationCodeName, operationDirection);

        return ResponseEntity.ok(new DataResponse(true));
    }

    @PostMapping("/check/phone/confirm")
    @Transactional
    public ResponseEntity<?> phoneConfirm(@RequestHeader("login") String login,
                                          @RequestHeader("password") String password,
                                          @Valid @RequestBody LoginAndSecretCodeRequest loginAndSecretCodeRequest) {
        if (!externalSystemServices.validateDate(login, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Неверные данные"));
        }

        String userPhone = loginAndSecretCodeRequest.getLogin();
        Account accountByPhone = accountService.findAccountByPhone(userPhone);

        //Если найденный аккаунт пустой, то смотрим, вдруг
        if (accountByPhone == null) {
            if (userPhone.startsWith("+7")) {
                // Создаём новый номер, заменяя "+7" на "8"
                String alternativePhone = "8" + userPhone.substring(2);
                accountByPhone = accountService.findAccountByPhone(alternativePhone);
                if (accountByPhone == null) {
                    // Создаём новый номер, заменяя "+7" на "7"
                    alternativePhone = "7" + userPhone.substring(2);
                    accountByPhone = accountService.findAccountByPhone(alternativePhone);
                }
            } else if (userPhone.startsWith("8")) {
                // Создаём новый номер, заменяя "8" на "+7"
                String alternativePhone = "+7" + userPhone.substring(1);
                accountByPhone = accountService.findAccountByPhone(alternativePhone);
                if (accountByPhone == null) {
                    // Создаём новый номер, заменяя "8" на "7"
                    alternativePhone = "7" + userPhone.substring(1);
                    accountByPhone = accountService.findAccountByPhone(alternativePhone);
                }
            } else if (userPhone.startsWith("7")) {
                // Создаём новый номер, заменяя "7" на "8"
                String alternativePhone = "8" + userPhone.substring(1);
                accountByPhone = accountService.findAccountByPhone(alternativePhone);
                if (accountByPhone == null) {
                    // Создаём новый номер, заменяя "7" на "+7"
                    alternativePhone = "+7" + userPhone.substring(1);
                    accountByPhone = accountService.findAccountByPhone(alternativePhone);
                }
            }
        }

        if (accountByPhone != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Телефон '" + userPhone + "' не найден в базе данных."));
        }

        String userCode = loginAndSecretCodeRequest.getCodeToken();

        List<OperationPincode> previousUserCodeOperations = operationCodeService.findAllByOperationNameDirectionAndDateAfter(userPhone, 10, "Пин-код не соответствует", "Подтверждение телефона");

        if (previousUserCodeOperations.size() >= 5) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Вы использовали слишком много попыток ввода кода за последние 10 минут"));
        }

        //Достаём последний пинкод за 10 минут
        var latestCode = operationCodeService.findLastCodeByPhoneAndDateAfter(userPhone, 10, "Формирование пин-кода", "Подтверждение телефона");

        String confirmationCodeFromDB = latestCode.getPinCodeData();
        if (!(userCode.equals(confirmationCodeFromDB))) {
            //Сохраняем что пользователь ввёл неверный код
            CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Подтверждение телефона");
            OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код не соответствует");

            operationCodeService.saveNewPinCodeOperation(userPhone, userCode, operationCodeName, operationDirection);

            //Код подтверждения не совпадает
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка введённых данных для телефона " + userPhone));
        } else {

            CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Подтверждение телефона");
            OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код соответствует");

            operationCodeService.saveNewPinCodeOperation(userPhone, userCode, operationCodeName, operationDirection);
            return ResponseEntity.ok(new BusBonusIDResponse(accountByPhone.getBusBonusId()));
        }
    }

    @PostMapping("/tickets/new")
    @Transactional
    public ResponseEntity<?> saveSeveralTickets(@RequestHeader("login") String login,
                                                @RequestHeader("password") String password,
                                                @Valid @RequestBody NewTicketsRequest newTicketsRequest) {
        if (!externalSystemServices.validateDate(login, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Неверные данные"));
        }

        if (newTicketsRequest.getUserTicketByBBIdList().size() > 10) {
            return ResponseEntity.badRequest().body(new MessageResponse("За раз нельзя отправлять больше 10 билетов. Пожалуйста, учитывайте это."));
        }
        Account account = accountService.getAccountByBBId(newTicketsRequest.getBusBonusId());
        accountTicketsService.saveNewUserTicketsFromExternalSystem(account, newTicketsRequest);
        return ResponseEntity.ok(new DataResponse(true));
    }
}
