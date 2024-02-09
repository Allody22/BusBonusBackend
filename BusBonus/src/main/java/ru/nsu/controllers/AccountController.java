package ru.nsu.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.model.DocumentTypes;
import ru.nsu.model.Documents;
import ru.nsu.model.TicketCategories;
import ru.nsu.model.user.Account;
import ru.nsu.model.user.UserData;
import ru.nsu.payload.request.AddCompanionRequest;
import ru.nsu.payload.request.DeleteCompanionRequest;
import ru.nsu.payload.request.UpdateAccountDocumentsRequest;
import ru.nsu.payload.request.UpdateOwnerDataRequest;
import ru.nsu.payload.response.AccountInfoResponse;
import ru.nsu.payload.response.DataResponse;
import ru.nsu.payload.response.MessageResponse;
import ru.nsu.payload.response.UserDataResponse;
import ru.nsu.services.AccountService;
import ru.nsu.services.OperationAccountService;
import ru.nsu.services.UserDataService;
import ru.nsu.services.UserDetailsImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
public class AccountController {

    private final AccountService accountService;

    private final OperationAccountService operationAccountService;

    private final UserDataService userDataService;

    @Autowired
    public AccountController(
            OperationAccountService operationAccountService,
            UserDataService userDataService,
            AccountService accountService) {
        this.userDataService = userDataService;
        this.operationAccountService = operationAccountService;
        this.accountService = accountService;
    }


    @GetMapping("/get_account")
    @Transactional
    public ResponseEntity<?> getAccount(@NotBlank @RequestParam(value = "phone") String phone) {
        Account account = accountService.getAccountByPhone(phone);
        if (account == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Аккаунта с номером телефона " + phone + " не существует"));
        }
        List<UserDataResponse> userDataResponseArrayList = new ArrayList<>();
        UserData ownerData = account.getOwnerData();

        List<Documents> userDocumentsList = ownerData.getDocuments();
        Documents ownerDocuments = userDocumentsList.get(userDocumentsList.size() - 1);

        UserDataResponse ownerDataResponse = new UserDataResponse(ownerData.getName(), ownerData.getLastName(),
                ownerData.getPatronymic(), ownerData.getBirthDate(), ownerData.getGender(),
                "Owner", account.getStatus().toString(),
                ownerDocuments.getDocumentNumber(), ownerDocuments.getDocumentSeries(), ownerDocuments.getIssuingAuthority(),
                ownerDocuments.getIssueDate(), ownerDocuments.getExpirationDate(), ownerDocuments.getAdditionalData(),
                ownerDocuments.getCitizenship(), ownerDocuments.getConfirmationOfBenefits(),
                Optional.of(ownerDocuments).map(Documents::getDocumentType).map(DocumentTypes::getType).orElse(null),
                Optional.of(ownerDocuments).map(Documents::getTicketCategory).map(TicketCategories::getCategory).orElse(null));
        userDataResponseArrayList.add(ownerDataResponse);

        List<UserData> currentUserDataList = account.getUserData();
        if (!currentUserDataList.isEmpty()) {
            for (var userData : currentUserDataList) {
                List<Documents> compaionDocumentsList = userData.getDocuments();
                Documents documents = compaionDocumentsList.get(compaionDocumentsList.size() - 1);

                UserDataResponse userDataResponse = new UserDataResponse(userData.getName(), userData.getLastName(),
                        userData.getPatronymic(), userData.getBirthDate(), userData.getGender(),
                        "Companion", account.getStatus().toString(),
                        documents.getDocumentNumber(), documents.getDocumentSeries(), documents.getIssuingAuthority(),
                        documents.getIssueDate(), documents.getExpirationDate(), documents.getAdditionalData(),
                        documents.getCitizenship(), documents.getConfirmationOfBenefits(),
                        Optional.of(documents).map(Documents::getDocumentType).map(DocumentTypes::getType).orElse(null),
                        Optional.of(documents).map(Documents::getTicketCategory).map(TicketCategories::getCategory).orElse(null));
                userDataResponseArrayList.add(userDataResponse);
            }
        }

        AccountInfoResponse accountInfoResponse = new AccountInfoResponse(account.getPhone(), account.getBusBonusId(),
                account.getEmail(), userDataResponseArrayList);

        return ResponseEntity.ok(accountInfoResponse);
    }



    @GetMapping("/get_account_jwt")
    @Transactional
    public ResponseEntity<?> getAccountByJWT() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountService.getAccountById(userDetails.getId());
        List<UserDataResponse> userDataResponseArrayList = new ArrayList<>();
        UserData ownerData = account.getOwnerData();

        //TODO возвращать все версии документов?
        List<Documents> ownerDataDocuments = ownerData.getDocuments();
        Documents ownerDocuments = ownerDataDocuments.get(ownerDataDocuments.size() - 1);
        UserDataResponse ownerDataResponse = new UserDataResponse(ownerData.getName(), ownerData.getLastName(),
                ownerData.getPatronymic(), ownerData.getBirthDate(), ownerData.getGender(),
                "Owner", account.getStatus().toString(),
                ownerDocuments.getDocumentNumber(), ownerDocuments.getDocumentSeries(), ownerDocuments.getIssuingAuthority(),
                ownerDocuments.getIssueDate(), ownerDocuments.getExpirationDate(), ownerDocuments.getAdditionalData(),
                ownerDocuments.getCitizenship(), ownerDocuments.getConfirmationOfBenefits(),
                Optional.of(ownerDocuments).map(Documents::getDocumentType).map(DocumentTypes::getType).orElse(null),
                Optional.of(ownerDocuments).map(Documents::getTicketCategory).map(TicketCategories::getCategory).orElse(null));
        userDataResponseArrayList.add(ownerDataResponse);

        List<UserData> currentUserDataList = account.getUserData();
        if (!currentUserDataList.isEmpty()) {
            for (var userData : currentUserDataList) {
                List<Documents> compaionDocumentsList = userData.getDocuments();
                Documents documents = compaionDocumentsList.get(compaionDocumentsList.size() - 1);

                UserDataResponse userDataResponse = new UserDataResponse(userData.getName(), userData.getLastName(),
                        userData.getPatronymic(), userData.getBirthDate(), userData.getGender(),
                        "Companion", account.getStatus().toString(),
                        documents.getDocumentNumber(), documents.getDocumentSeries(), documents.getIssuingAuthority(),
                        documents.getIssueDate(), documents.getExpirationDate(), documents.getAdditionalData(),
                        documents.getCitizenship(), documents.getConfirmationOfBenefits(),
                        Optional.of(documents).map(Documents::getDocumentType).map(DocumentTypes::getType).orElse(null),
                        Optional.of(documents).map(Documents::getTicketCategory).map(TicketCategories::getCategory).orElse(null));
                userDataResponseArrayList.add(userDataResponse);
            }
        }

        AccountInfoResponse accountInfoResponse = new AccountInfoResponse(account.getPhone(), account.getBusBonusId(),
                account.getEmail(), userDataResponseArrayList);

        return ResponseEntity.ok(accountInfoResponse);
    }


    @PostMapping("/owner/update_documents")
    @Transactional
    public ResponseEntity<?> updateOwnerDocuments(@Valid @RequestBody UpdateAccountDocumentsRequest updateAccountDocumentsRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountService.getAccountById(userDetails.getId());
        UserData ownerUserData = accountService.getOwnerUserData(account);

        String description = accountService.updateUserDocumentByUserId(ownerUserData.getId(), updateAccountDocumentsRequest.getDocumentType(),
                updateAccountDocumentsRequest.getDocumentNumber(), updateAccountDocumentsRequest.getDocumentSeries(), updateAccountDocumentsRequest.getIssuingAuthority(),
                updateAccountDocumentsRequest.getIssueDate(), updateAccountDocumentsRequest.getExpirationDate(), updateAccountDocumentsRequest.getAdditionalData(),
                updateAccountDocumentsRequest.getCitizenship(), updateAccountDocumentsRequest.getConfirmationOfBenefits(), updateAccountDocumentsRequest.getTicketCategory());

        operationAccountService.saveNewAccountOperation(account, "Владелец аккаунта меняет информацию о своих документах",
                description);

        return ResponseEntity.ok(new DataResponse(true));
    }


    @PostMapping("/owner/update_data")
    @Transactional
    public ResponseEntity<?> updateInfo(@Valid @RequestBody UpdateOwnerDataRequest updateOwnerDataRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountService.getAccountById(userDetails.getId());
        UserData ownerUserData = accountService.getOwnerUserData(account);

        String descriptionUser = userDataService.updateUserData(updateOwnerDataRequest.getName(),
                updateOwnerDataRequest.getLastName(), updateOwnerDataRequest.getPatronymic(),
                updateOwnerDataRequest.getBirthDate(), updateOwnerDataRequest.getGender(), ownerUserData.getId());

        operationAccountService.saveNewAccountOperation(account, "Владелец аккаунта меняет информацию о себе",
                descriptionUser);

        return ResponseEntity.ok(new DataResponse(true));
    }


    @PostMapping("/add_companion")
    @Transactional
    public ResponseEntity<?> addCompanion(@Valid @RequestBody AddCompanionRequest addCompanionRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account userAccount = accountService.getAccountById(userDetails.getId());

        String description = userDataService.addCompanion(userAccount, addCompanionRequest.getGender(), addCompanionRequest.getName(),
                addCompanionRequest.getBirthDate(), addCompanionRequest.getLastName(), addCompanionRequest.getPatronymic(),
                addCompanionRequest.getTicketCategory(), addCompanionRequest.getDocumentNumber(),
                addCompanionRequest.getDocumentSeries(), addCompanionRequest.getAdditionalData(),
                addCompanionRequest.getCitizenship(), addCompanionRequest.getIssueDate(),
                addCompanionRequest.getIssuingAuthority(), addCompanionRequest.getExpirationDate(),
                addCompanionRequest.getConfirmationOfBenefits(), addCompanionRequest.getDocumentType());

        operationAccountService.saveNewAccountOperation(userAccount, "Добавление попутчика в аккаунт",
                description);

        return ResponseEntity.ok(new DataResponse(true));
    }



    @PostMapping("/delete_companion")
    @Transactional
    public ResponseEntity<?> deleteCompanion(@Valid @RequestBody DeleteCompanionRequest deleteCompanionRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account userAccount = accountService.getAccountById(userDetails.getId());


        DocumentTypes documentTypes = userDataService.findDocumentType(deleteCompanionRequest.getDocumentType());
        userDataService.deleteCompanionsByDocumentsAndNameWithoutAccount(deleteCompanionRequest.getName(), deleteCompanionRequest.getLastName(),
                deleteCompanionRequest.getPatronymic(), deleteCompanionRequest.getCitizenship(), documentTypes,
                deleteCompanionRequest.getDocumentNumber(), userAccount, deleteCompanionRequest.getDocumentSeries());

        operationAccountService.saveNewAccountOperation(userAccount, "Удаление попутчика из аккаунта",
                "Попутчик с документом " + deleteCompanionRequest.getDocumentType() + ", номером: " + deleteCompanionRequest.getDocumentNumber() + ", серией: " + deleteCompanionRequest.getDocumentSeries()
                        + " удалён из аккаунта");

        return ResponseEntity.ok(new DataResponse(true));
    }
}
