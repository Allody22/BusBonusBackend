package ru.nsu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.exceptions.AccountNotFoundException;
import ru.nsu.exceptions.NotInDataBaseException;
import ru.nsu.model.DocumentTypes;
import ru.nsu.model.Documents;
import ru.nsu.model.Role;
import ru.nsu.model.TicketCategories;
import ru.nsu.model.constants.ERole;
import ru.nsu.model.constants.EUserTypeStatus;
import ru.nsu.model.user.Account;
import ru.nsu.model.user.UserData;
import ru.nsu.repository.DocumentTypesRepository;
import ru.nsu.repository.DocumentsRepository;
import ru.nsu.repository.TicketCategoriesRepository;
import ru.nsu.repository.user.AccountRepository;
import ru.nsu.repository.user.RoleRepository;
import ru.nsu.repository.user.UserDataRepository;

import java.util.*;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;

    private final UserDataRepository userDataRepository;


    private final TicketCategoriesRepository ticketCategoriesRepository;

    private final DocumentTypesRepository documentTypesRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserDataRepository userDataRepository,
                          PasswordEncoder encoder, RoleRepository roleRepository, TicketCategoriesRepository ticketCategoriesRepository,
                          DocumentTypesRepository documentTypesRepository) {
        this.accountRepository = accountRepository;
        this.ticketCategoriesRepository = ticketCategoriesRepository;
        this.documentTypesRepository = documentTypesRepository;
        this.roleRepository = roleRepository;
        this.userDataRepository = userDataRepository;
        this.encoder = encoder;
    }

    public String generateRandomPassword() {
        Random random = new Random();
        int randomCode = random.nextInt(1000000);
        return String.format("%06d", randomCode);
    }

    public List<String> getAllDocumentTypes() {
        return documentTypesRepository.getAll();
    }

    public List<String> getAllTicketCategories() {
        return ticketCategoriesRepository.getAll();
    }

    public void updateAccountPassword(Long accountId, String password) {
        accountRepository.updatePasswordById(accountId, encoder.encode(password));
    }

    public boolean updateAccountPasswordFromPrevious(Account account, String newPassword, String oldPassword) {
        if (encoder.matches(oldPassword, account.getPassword())) {
            accountRepository.updatePasswordById(account.getId(), encoder.encode(newPassword));
            return true;
        }
        return false;
    }

    public Account getAccountByBysBonus(String busBonusId) {
        return accountRepository.getAccountByBusBonusId(busBonusId)
                .orElse(null);
    }

    public boolean checkAccExistenceByPhone(String phone) {
        return accountRepository.existsAccountByPhone(phone);
    }

    public Account getAccountByPhone(String phone) {
        return accountRepository.getAccountByPhone(phone)
                .orElse(null);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.getAccountById(accountId)
                .orElse(null);
    }

    public UserData getOwnerUserData(Account account) {
        var ownerUserData = account.getOwnerData();
        if (ownerUserData != null) {
            return account.getOwnerData();
        } else {
            throw new NotInDataBaseException("В базе данных аккаунтов не найден глава аккаунта с телефоном: " + account.getPhone());
        }
    }


    @Transactional
    public String updateUserDocumentByUserId(Long userDataId, String documentType, String documentNumber, String documentSeries,
                                             String issuingAuthority, Date issueDate, Date expirationDate, String additionalData,
                                             String citizenship, String confirmationOfBenefits, String ticketCategory) {
        DocumentTypes foundedDocumentType = documentTypesRepository.findByType(documentType)
                .orElseThrow(() -> new NotInDataBaseException("В базе данных типов документов не найден тип: " + documentType));

        TicketCategories foundedTicketCategory = ticketCategoriesRepository.findTicketCategoriesByCategory(ticketCategory)
                .orElseThrow(() -> new NotInDataBaseException("В базе данных категорий билетов не найдена категория: " + ticketCategory));
        UserData userData = userDataRepository.findById(userDataId)
                .orElseThrow(AccountNotFoundException::new);
        List<Documents> userDocumentsList = userData.getDocuments();
        Documents userPreviousDocuments = userDocumentsList.get(userDocumentsList.size() - 1);

        //TODO проверка обязательных полей
        Documents newDocuments = new Documents();
        newDocuments.setDocumentNumber(documentNumber);
        newDocuments.setDocumentType(foundedDocumentType);
        newDocuments.setDocumentSeries(documentSeries);
        newDocuments.setIssueDate(issueDate);
        newDocuments.setIssuingAuthority(issuingAuthority);
        newDocuments.setUser(userData);
        newDocuments.setAdditionalData(additionalData);
        newDocuments.setCitizenship(citizenship);
        newDocuments.setExpirationDate(expirationDate);
        newDocuments.setConfirmationOfBenefits(confirmationOfBenefits);
        newDocuments.setTicketCategory(foundedTicketCategory);
        newDocuments.setDate(new Date());
        newDocuments.setVersion((userPreviousDocuments.getVersion() + 1));

        userData.addDocumentVersion(newDocuments);
        StringBuilder descriptionMessage = new StringBuilder("Глава аккаунта указал");

        int initialLength = descriptionMessage.length();

        if (documentType != null && foundedDocumentType != null && !foundedDocumentType.equals(userPreviousDocuments.getDocumentType())) {
            descriptionMessage.append(" новый тип документа: '")
                    .append(documentType)
                    .append("',");
        }
        if (documentNumber != null && !documentNumber.equals(userPreviousDocuments.getDocumentNumber())) {
            descriptionMessage.append(" новый номер документа: '")
                    .append(documentNumber)
                    .append("',");
        }
        if (ticketCategory != null && foundedTicketCategory != null && !foundedTicketCategory.equals(userPreviousDocuments.getTicketCategory())) {
            descriptionMessage.append(" новый тип билета: '")
                    .append(ticketCategory)
                    .append("',");
        }
        if (documentSeries != null && !documentSeries.equals(userPreviousDocuments.getDocumentSeries())) {
            descriptionMessage.append(" новую серию документа: '")
                    .append(documentSeries)
                    .append("',");
        }
        if (issuingAuthority != null && !issuingAuthority.equals(userPreviousDocuments.getIssuingAuthority())) {
            descriptionMessage.append(" новый орган выдавший документ: '")
                    .append(issuingAuthority)
                    .append("',");
        }
        if (issueDate != null && !issueDate.equals(userPreviousDocuments.getIssueDate())) {
            descriptionMessage.append(" новую дату получения документов: '")
                    .append(issueDate)
                    .append("',");
        }
        if (expirationDate != null && !expirationDate.equals(userPreviousDocuments.getExpirationDate())) {
            descriptionMessage.append(" новую дату конца действия документов: '")
                    .append(expirationDate)
                    .append("',");
        }
        if (citizenship != null && !citizenship.equals(userPreviousDocuments.getCitizenship())) {
            descriptionMessage.append(" новое гражданство: '")
                    .append(citizenship)
                    .append("',");
        }
        if (confirmationOfBenefits != null && !confirmationOfBenefits.equals(userPreviousDocuments.getConfirmationOfBenefits())) {
            descriptionMessage.append(" новый льготный документ: '")
                    .append(confirmationOfBenefits)
                    .append("',");
        }
        if (additionalData != null && !additionalData.equals(userPreviousDocuments.getAdditionalData())) {
            descriptionMessage.append(" новую дополнительную информацию: '")
                    .append(additionalData)
                    .append("',");
        }

        if (descriptionMessage.length() > initialLength) {
            if (descriptionMessage.charAt(descriptionMessage.length() - 1) == ',') {
                descriptionMessage.deleteCharAt(descriptionMessage.length() - 1);
            }
            return descriptionMessage.toString();
        } else {
            return "Никаких изменений в документах не было";
        }
    }

    @Transactional
    public void updateAccountPhoneAndEmailByAccountId(Long accountId, String phone, String email) {
        accountRepository.updateAccountPhoneAndEmailById(accountId, phone, email);
    }

    @Transactional
    public void updateAccountEmailByAccountId(Long accountId, String email) {
        accountRepository.updateAccountEmailById(accountId, email);
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    // Это проверка существования аккаунта и подтверждения. True - если акк есть и подтверждён, а иначе false
    public boolean checkAccStatus(String phone) {
        Account account = getAccountByPhone(phone);
        if (account != null) {
            UserData potentialUserDataByPhone = account.getOwnerData();
            if (potentialUserDataByPhone != null) {
                if (account.getStatus() == EUserTypeStatus.NOT_ACTIVE) {
                    return false;
                } else return account.getStatus() == EUserTypeStatus.ACTIVE;
            }
        }
        return false;
    }

    // Это проверка существования аккаунта и подтверждения. True - если акк есть и подтверждён, а иначе false
    public boolean checkAccStatusAndExistence(String phone) {
        Account account = getAccountByPhone(phone);
        if (account != null) {
            UserData potentialOwnerUserDataByPhone = account.getOwnerData();
            if (potentialOwnerUserDataByPhone != null) {
                if (account.getStatus() == EUserTypeStatus.NOT_ACTIVE) {
                    return false;
                } else if (account.getStatus() == EUserTypeStatus.ACTIVE) {
                    return true;
                }
            }
        }
        return true;
    }

    @Transactional
    public Account createNewAccountFromSite(String phone, String password) {
        Account newAccount = new Account();
        newAccount.setPhone(phone);

        //Ставим что у нас новый еще не подтверждённый аккаунт
        newAccount.setStatus(EUserTypeStatus.NOT_ACTIVE);
        newAccount.setPassword(encoder.encode(password));

        // У пользователя права только обычного юзера
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new NotInDataBaseException("В базе данных ролей не найдена роль: " + ERole.ROLE_USER.name()));
        roles.add(userRole);
        newAccount(roles);

        // А сейчас его создаёт будущий владелец
        UserData newOwnerUserData = new UserData();

        newOwnerUserData.setDate(new Date());
        newOwnerUserData.setOwnerAccount(newAccount);

        Documents newDocuments = new Documents();
        newDocuments.setVersion(1);

        newDocuments.setUser(newOwnerUserData);
        newOwnerUserData.addDocumentVersion(newDocuments);
        newAccount.setOwnerData(newOwnerUserData);

        return accountRepository.save(newAccount);
    }
}
