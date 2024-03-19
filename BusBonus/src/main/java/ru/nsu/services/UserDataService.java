package ru.nsu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.exceptions.NotInDataBaseException;
import ru.nsu.model.Documents;
import ru.nsu.model.constants.DocumentTypes;
import ru.nsu.model.constants.EUserTypeStatus;
import ru.nsu.model.constants.TicketCategories;
import ru.nsu.model.user.Account;
import ru.nsu.model.user.UserData;
import ru.nsu.repository.DocumentTypesRepository;
import ru.nsu.repository.DocumentsRepository;
import ru.nsu.repository.TicketCategoriesRepository;
import ru.nsu.repository.user.AccountRepository;
import ru.nsu.repository.user.UserDataRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserDataService {

    private final AccountRepository accountRepository;

    private final UserDataRepository userDataRepository;

    private final DocumentsRepository documentsRepository;

    private final DocumentTypesRepository documentTypesRepository;

    private final TicketCategoriesRepository ticketCategoriesRepository;

    @Autowired
    public UserDataService(AccountRepository accountRepository, UserDataRepository userDataRepository, TicketCategoriesRepository ticketCategoriesRepository,
                           DocumentsRepository documentsRepository, DocumentTypesRepository documentTypesRepository) {
        this.accountRepository = accountRepository;
        this.ticketCategoriesRepository = ticketCategoriesRepository;
        this.documentTypesRepository = documentTypesRepository;
        this.documentsRepository = documentsRepository;
        this.userDataRepository = userDataRepository;
    }

    @Transactional
    public String updateUserData(String name, String lastName,
                                 String patronymic, Date birthDate,
                                 String gender, Long dataId) {
        UserData userData = userDataRepository.findById(dataId)
                .orElseThrow();
        userDataRepository.updateUserData(name, lastName, patronymic, birthDate,
                gender, dataId);

        StringBuilder descriptionMessage = new StringBuilder("Глава аккаунта указал");

        int initialLength = descriptionMessage.length();

        if (name != null && !name.equals(userData.getName())) {
            descriptionMessage.append(" новое имя: '")
                    .append(name)
                    .append("',");
        }

        if (lastName != null && !lastName.equals(userData.getLastName())) {
            descriptionMessage.append(" новую фамилию: '")
                    .append(lastName)
                    .append("',");
        }

        if (patronymic != null && !patronymic.equals(userData.getPatronymic())) {
            descriptionMessage.append(" новое отчество: '")
                    .append(patronymic)
                    .append("',");
        }
        if (birthDate != null && !birthDate.equals(userData.getBirthDate())) {
            descriptionMessage.append(" новую дату рождения: '")
                    .append(birthDate)
                    .append("',");
        }
        if (gender != null && !gender.equals(userData.getGender())) {
            descriptionMessage.append(" новый гендер: '")
                    .append(gender)
                    .append("',");
        }
        if (descriptionMessage.length() > initialLength) {
            if (descriptionMessage.charAt(descriptionMessage.length() - 1) == ',') {
                descriptionMessage.deleteCharAt(descriptionMessage.length() - 1);
            }
            return descriptionMessage.toString();
        } else {
            return "Никаких изменений в информации о человеке не было";
        }
    }


    //Возвращаем true - если все аккаунты подтверждены, иначе false
    public boolean checkAccountActive(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElse(null);
        if (account == null) {
            return false;
        } else {
            return account.getStatus().equals(EUserTypeStatus.ACTIVE);
        }
    }

    public UserData findOwnerByBusBonusId(String busBonusId) {
        Account account = accountRepository.getAccountByBusBonusId(busBonusId)
                .orElse(null);
        if (account != null) {
            return account.getOwnerData();
        } else {
            return null;
        }
    }

    public DocumentTypes findDocumentType(String type) {
        return documentTypesRepository.findByType(type)
                .orElse(null);
    }

    @Transactional
    public String addCompanion(Account account, String gender, String name, Date birthDate,
                               String lastName, String patronymic, String ticketCategoryString,
                               String documentNumber, String documentSeries,
                               String additionalData, String citizenship,
                               Date issueDate, String issuingAuthority, Date expirationDate,
                               String confirmationOfBenefits, String documentType) {
        UserData companionData = new UserData();
        companionData.setDate(new Date());
        companionData.setGender(gender);
        companionData.setName(name);
        companionData.setBirthDate(birthDate);
        companionData.setLastName(lastName);
        companionData.setPatronymic(patronymic);

        Documents companionDocuments = new Documents();
        TicketCategories ticketCategory = ticketCategoriesRepository.findTicketCategoriesByCategory(ticketCategoryString)
                .orElseThrow(() -> new NotInDataBaseException("В базе данных категорий билетов не найдена категория: " + ticketCategoryString));
        companionDocuments.setTicketCategory(ticketCategory);
        companionDocuments.setDocumentNumber(documentNumber);
        companionDocuments.setDocumentSeries(documentSeries);
        companionDocuments.setAdditionalData(additionalData);
        companionDocuments.setCitizenship(citizenship);
        companionDocuments.setIssueDate(issueDate);
        companionDocuments.setIssuingAuthority(issuingAuthority);
        companionDocuments.setExpirationDate(expirationDate);
        companionDocuments.setConfirmationOfBenefits(confirmationOfBenefits);
        companionDocuments.setVersion(1);
        companionDocuments.setDate(new Date());
        DocumentTypes documentTypes = findDocumentType(documentType);
        companionDocuments.setDocumentType(documentTypes);
        companionDocuments.setUser(companionData);

        documentsRepository.save(companionDocuments);
        companionData.addDocumentVersion(companionDocuments);
        account.addUserData(companionData);

        userDataRepository.save(companionData);

        StringBuilder descriptionMessage = new StringBuilder("Добавлен попутчик с");

        int initialLength = descriptionMessage.length();

        if (name != null && !name.isBlank()) {
            descriptionMessage.append(" именем: '")
                    .append(name)
                    .append("',");
        }

        if (lastName != null && !lastName.isBlank()) {
            descriptionMessage.append(" фамилией: '")
                    .append(lastName)
                    .append("',");
        }

        if (patronymic != null && !patronymic.isBlank()) {
            descriptionMessage.append(" отчеством: '")
                    .append(patronymic)
                    .append("',");
        }

        if (gender != null && !gender.isBlank()) {
            descriptionMessage.append(" гендером: '")
                    .append(gender)
                    .append("',");
        }

        if (birthDate != null) {
            descriptionMessage.append(" датой рождения: '")
                    .append(birthDate)
                    .append("',");
        }

        if (ticketCategoryString != null && !ticketCategoryString.isBlank()) {
            descriptionMessage.append(" категорией билетов: '")
                    .append(ticketCategoryString)
                    .append("',");
        }

        if (documentTypes != null && !documentTypes.getType().isBlank()) {
            descriptionMessage.append(" типом документа: '")
                    .append(documentTypes.getType())
                    .append("',");
        }

        if (documentNumber != null && !documentNumber.isBlank()) {
            descriptionMessage.append(" номером документов: '")
                    .append(documentNumber)
                    .append("',");
        }

        if (documentSeries != null && !documentSeries.isBlank()) {
            descriptionMessage.append(" серией документов: '")
                    .append(documentSeries)
                    .append("',");
        }

        if (additionalData != null && !additionalData.isBlank()) {
            descriptionMessage.append(" дополнительной информацией: '")
                    .append(additionalData)
                    .append("',");
        }

        if (citizenship != null && !citizenship.isBlank()) {
            descriptionMessage.append(" гражданством: '")
                    .append(citizenship)
                    .append("',");
        }

        if (issueDate != null) {
            descriptionMessage.append(" датой начала действия документов: '")
                    .append(issueDate)
                    .append("',");
        }

        if (issuingAuthority != null && !issuingAuthority.isBlank()) {
            descriptionMessage.append(" место, выдачи документов: '")
                    .append(issuingAuthority)
                    .append("',");
        }
        if (expirationDate != null) {
            descriptionMessage.append(" датой конца действия документов: '")
                    .append(expirationDate)
                    .append("',");
        }
        if (confirmationOfBenefits != null && !confirmationOfBenefits.isBlank()) {
            descriptionMessage.append(" льготным документом: '")
                    .append(confirmationOfBenefits)
                    .append("',");
        }
        if (descriptionMessage.length() > initialLength) {
            if (descriptionMessage.charAt(descriptionMessage.length() - 1) == ',') {
                descriptionMessage.deleteCharAt(descriptionMessage.length() - 1);
            }
            return descriptionMessage.toString();
        } else {
            return "Никакой информации о попутчике нет";
        }
    }

    @Transactional
    public void removeAllAccountCompanions(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElse(null);
        if (account == null) {
            return;
        }

        // Очистка коллекции userData удаляет связи в таблице ManyToMany
        account.getUserData().clear();
        // Сохранение изменений в аккаунте, JPA обновит связующую таблицу account_companions
        accountRepository.save(account);
    }

    @Transactional
    public void removeCompanionFromAccount(Long userDataId, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        account.getUserData().removeIf(userData -> userData.getId().equals(userDataId));

        accountRepository.save(account);
    }

    //Удаляем всех попутчиков с определёнными данными из аккаунта
    @Transactional
    public void deleteCompanionsByDocumentsAndNameWithoutAccount(String name, String lastName, String patronymic,
                                                                 String citizenship, DocumentTypes documentType, String documentNumber,
                                                                 Account account, String documentSeries) {
        List<UserData> companionsList = account.getUserData();
        List<UserData> toRemoveWithoutOwner = new ArrayList<>();
        List<UserData> toRemoveWithOwner = new ArrayList<>();

        if (!companionsList.isEmpty()) {
            for (var userData : companionsList) {
                if (userData != null) {
                    List<Documents> userDocumentsList = userData.getDocuments();
                    Documents userDocuments = userDocumentsList.get(userDocumentsList.size() - 1);
                    if (userDocuments != null && userData.getName() != null && userData.getLastName() != null
                            && userData.getPatronymic() != null && userDocuments.getCitizenship() != null
                            && userDocuments.getDocumentNumber() != null && userDocuments.getDocumentType() != null
                            && userDocuments.getDocumentSeries() != null) {
                        if (userData.getName().equals(name) && userData.getLastName().equals(lastName)
                                && userData.getPatronymic().equals(patronymic) && userDocuments.getCitizenship().equals(citizenship)
                                && userDocuments.getDocumentNumber().equals(documentNumber)
                                && userDocuments.getDocumentType().equals(documentType) && userDocuments.getDocumentSeries().equals(documentSeries)) {
                            if (userData.getOwnerAccount() == null) {
                                toRemoveWithoutOwner.add(userData);
                            } else {
                                toRemoveWithOwner.add(userData);
                            }
                        }
                    }
                }
            }
        }
        if (!toRemoveWithoutOwner.isEmpty()) {
            account.getUserData().removeAll(toRemoveWithoutOwner);
            accountRepository.save(account);

            for (UserData userData : toRemoveWithoutOwner) {
                userDataRepository.deleteById(userData.getId());
            }
        }
        if (!toRemoveWithOwner.isEmpty()) {
            account.getUserData().removeAll(toRemoveWithOwner);
            accountRepository.save(account);
        }
    }

}
