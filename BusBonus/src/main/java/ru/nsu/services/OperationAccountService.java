package ru.nsu.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.model.operations.MethodList;
import ru.nsu.model.operations.OperationAccount;
import ru.nsu.model.user.Account;
import ru.nsu.repository.operation.MethodListRepository;
import ru.nsu.repository.operation.OperationAccountRepository;
import ru.nsu.repository.user.AccountRepository;

import java.util.Date;

@Service
@Slf4j
public class OperationAccountService {

    private final AccountRepository accountRepository;

    private final MethodListRepository methodListRepository;

    private final OperationAccountRepository operationAccountRepository;

    @Autowired
    public OperationAccountService(AccountRepository accountRepository, MethodListRepository methodListRepository,
                                   OperationAccountRepository operationAccountRepository) {
        this.accountRepository = accountRepository;
        this.operationAccountRepository = operationAccountRepository;
        this.methodListRepository = methodListRepository;
    }

    @Transactional
    public void saveNewAccountOperation(Account userAccount, String methodName, String description) {
        MethodList methodList = methodListRepository.findByMethodName(methodName)
                .orElseThrow();
        OperationAccount operationAccount = new OperationAccount();
        operationAccount.setDate(new Date());
        operationAccount.setDescription(description);
        operationAccount.setAccountRef(userAccount);
        operationAccount.setMethodNameRef(methodList);
        operationAccountRepository.save(operationAccount);
    }

}
