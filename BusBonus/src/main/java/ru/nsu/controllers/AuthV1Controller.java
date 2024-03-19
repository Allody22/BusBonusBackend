package ru.nsu.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.model.operations.CodeOperationDirection;
import ru.nsu.model.operations.OperationCodeNames;
import ru.nsu.model.operations.OperationPincode;
import ru.nsu.model.user.Account;
import ru.nsu.payload.request.*;
import ru.nsu.payload.response.BusBonusIDResponse;
import ru.nsu.payload.response.DataResponse;
import ru.nsu.payload.response.MessageResponse;
import ru.nsu.services.RefreshTokenService;
import ru.nsu.services.UserDetailsImpl;
import ru.nsu.services.interfaces.IAccountService;
import ru.nsu.services.interfaces.IEmailService;
import ru.nsu.services.interfaces.IOperationAccountService;
import ru.nsu.services.interfaces.IOperationCodeService;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthV1Controller {

    private final IAccountService accountService;

    private final RefreshTokenService refreshTokenService;

    private final IOperationCodeService operationCodeService;

    private final IOperationAccountService operationAccountService;

    private final IEmailService emailService;


    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<?> logout() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenService.deleteAllByAccountId(userDetails.getId());

        return ResponseEntity.ok(new DataResponse(true));
    }

    @PostMapping("/change/password")
    @Transactional
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account userAccount = accountService.getAccountById(userDetails.getId());

        boolean result = accountService.updateAccountPasswordFromPrevious(userAccount, changePasswordRequest.getNewPassword(), changePasswordRequest.getOldPassword());
        if (!result) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Пожалуйста, проверьте введённые данные."));
        } else {
            operationAccountService.saveNewAccountOperation(userAccount, "Пользователь меняет пароль",
                    "Пользователь сменил пароль");
            return ResponseEntity.ok(new DataResponse(true));
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
        System.out.println("NEW PASSWORD FOR " + phone + ":" + accountCode);

        //Сохраняем действия в таблице операции с кодом
        CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Регистрация аккаунта");
        OperationCodeNames operationCodeName = operationCodeService.findOperationName("Формирование пин-кода");
        operationCodeService.saveNewPinCodeOperation(phone, accountCode, operationCodeName, operationDirection);
        return ResponseEntity.ok(new DataResponse(true));
    }


    @PostMapping("/registration/confirm")
    @Transactional
    public ResponseEntity<?> registrationConfirm(@Valid @RequestBody LoginAndSecretCodeRequest loginAndSecretCodeRequest) {
        String userPhone = loginAndSecretCodeRequest.getLogin();
        String userCode = loginAndSecretCodeRequest.getCodeToken();

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
            Account accountByPhone = accountService.createNewAccountFromSite(userPhone, userCode);

            operationAccountService.saveNewAccountOperation(accountByPhone, "Пользователь успешно регистрируется не с сайта",
                    "Пользователь успешно регистрируется не с сайта");

            CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Регистрация аккаунта");
            OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код соответствует");

            operationCodeService.saveNewPinCodeOperation(userPhone, userCode, operationCodeName, operationDirection);
            return ResponseEntity.ok(new BusBonusIDResponse(accountByPhone.getBusBonusId()));
        }
    }

    @PostMapping("/registration/check")
    @Transactional
    public ResponseEntity<?> registrationCheck(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new DataResponse(accountService.checkAccStatus(loginRequest.getLogin())));
    }

    @PostMapping("/change/email")
    @Transactional
    public ResponseEntity<?> changeEmail(@Valid @RequestBody EmailRequest emailRequest) throws Exception {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account userAccount = accountService.getAccountById(userDetails.getId());
        String userPhone = userAccount.getPhone();
        String userEmail = emailRequest.getEmail();

        if (accountService.checkAccExistenceByEmail(userEmail)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Такая почта уже зарегистрирована."));
        }

        List<OperationPincode> previousUserCodeOperations = operationCodeService.findAllByOperationNameDirectionAndDateAfterLikeUserLogin(userPhone, 60, "Формирование пин-кода", "Смена почты");
        if (previousUserCodeOperations.size() >= 3) {
            return ResponseEntity.badRequest().body(new MessageResponse("Превышено количество смс для смены телефона в час."));
        }

        String secretCode = accountService.generateRandomPassword();

        emailService.sendRegistrationMessage(userEmail, secretCode);

        log.info("NEW SECRET CODE FOR EMAIL " + userEmail + " CHANGE FOR " + userPhone + ":" + secretCode);

        CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Смена почты");
        OperationCodeNames operationCodeName = operationCodeService.findOperationName("Формирование пин-кода");
        operationCodeService.saveNewPinCodeOperation(userPhone + " " + userEmail, secretCode, operationCodeName, operationDirection);

        //Отправляем код подтверждения в СМС для пароля
        return ResponseEntity.ok(new DataResponse(true));
    }

    @PostMapping("/change/email/confirm")
    @Transactional
    public ResponseEntity<?> changeEmail(@Valid @RequestBody ChangeEmailRequest changeEmailRequest) throws Exception {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account userAccount = accountService.getAccountById(userDetails.getId());
        String userPhone = userAccount.getPhone();
        String newEmail = changeEmailRequest.getNewEmail();
        String userCode = changeEmailRequest.getCodeToken();

        List<OperationPincode> previousUserCodeOperations = operationCodeService.findAllByOperationNameDirectionAndDateAfterLikeUserLogin(userPhone, 60, "Пин-код не соответствует", "Смена почты");
        if (previousUserCodeOperations.size() >= 4) {
            return ResponseEntity.badRequest().body(new MessageResponse("Вы использовали слишком много неверных попыток ввода кода."));
        }

        //Достаём последний пинкод за 10 минут
        var latestCode = operationCodeService.findLastCodeByPhoneAndDateAfter(userPhone + " " + newEmail, 10, "Формирование пин-кода", "Смена почты");
        String confirmationCodeFromDB = latestCode.getPinCodeData();

        if (!(userCode.equals(confirmationCodeFromDB))) {
            //Сохраняем что пользователь ввёл неверный код
            CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Смена почты");
            OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код не соответствует");
            operationCodeService.saveNewPinCodeOperation(userPhone + " " + newEmail, userCode, operationCodeName, operationDirection);

            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка введённых данных. Внимательно проверьте их еще раз для телефона для почты " + newEmail));
        } else {
            Account accountByPhone = accountService.getAccountByPhone(userPhone);
            if (accountByPhone != null) {
                accountService.updateAccountEmailByAccountId(accountByPhone.getId(), newEmail);

                //В таблице операций личного кабинета пишем что чел ввёл правильный пинкод для смены телефона
                CodeOperationDirection operationDirection = operationCodeService.findOperationDirection("Смена почты");
                OperationCodeNames operationCodeName = operationCodeService.findOperationName("Пин-код соответствует");
                operationCodeService.saveNewPinCodeOperation(userPhone + " " + newEmail, userCode, operationCodeName, operationDirection);

                operationAccountService.saveNewAccountOperation(accountByPhone, "Пользователь меняет свой почтовый ящик",
                        "Пользователь получил почтовый ящик " + newEmail);
                emailService.sendConfirmationMessage(newEmail);
                return ResponseEntity.ok(new DataResponse(true));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Сначала отправьте запрос на получение пароля на почту"));
            }
        }
    }
}

