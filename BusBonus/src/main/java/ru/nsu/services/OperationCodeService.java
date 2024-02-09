package ru.nsu.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.exceptions.NotInDataBaseException;
import ru.nsu.model.operations.CodeOperationDirection;
import ru.nsu.model.operations.OperationCodeNames;
import ru.nsu.model.operations.OperationPincode;
import ru.nsu.repository.operation.CodeOperationDirectionRepository;
import ru.nsu.repository.operation.CodeOperationNamesRepository;
import ru.nsu.repository.operation.OperationAccountRepository;
import ru.nsu.repository.operation.OperationPinCodeRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OperationCodeService {

    private final OperationPinCodeRepository operationPinCodeRepository;

    private final CodeOperationNamesRepository codeOperationNamesRepository;

    private final CodeOperationDirectionRepository codeOperationDirectionRepository;

    @Autowired
    public OperationCodeService(OperationPinCodeRepository operationPinCodeRepository, OperationAccountRepository operationAccountRepository,
                                CodeOperationDirectionRepository codeOperationDirectionRepository, CodeOperationNamesRepository codeOperationNamesRepository) {
        this.codeOperationDirectionRepository = codeOperationDirectionRepository;
        this.codeOperationNamesRepository = codeOperationNamesRepository;
        this.operationPinCodeRepository = operationPinCodeRepository;
    }


    @Transactional
    public void saveNewPinCodeOperation(String login, String pinCodeData,
                                        OperationCodeNames operationName, CodeOperationDirection operationDirection) {
        OperationPincode newUserPinCode = new OperationPincode();
        newUserPinCode.setDate(new Date());
        newUserPinCode.setLoginUser(login);
        newUserPinCode.setOperationName(operationName);
        newUserPinCode.setOperationDirection(operationDirection);
        newUserPinCode.setPinCodeData(pinCodeData);

        operationPinCodeRepository.save(newUserPinCode);
    }

    public OperationCodeNames findOperationName(String operationName) {
        return codeOperationNamesRepository.findByName(operationName)
                .orElse(null);
    }

    public CodeOperationDirection findOperationDirection(String operationDirection) {
        return codeOperationDirectionRepository.findByDirection(operationDirection)
                .orElse(null);
    }


    @Transactional
    public OperationPincode findLastCodeByPhoneAndDateAfter(String phone, int minutesFromNow, String context, String operationDirection) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        cal.add(Calendar.MINUTE, -minutesFromNow);
        Date tenMinutesBefore = cal.getTime();

        //Если почему-то не найден код - ошибка
        return operationPinCodeRepository.findTopByLoginUserAndDateAfterAndOperationDirection(phone, context, operationDirection, tenMinutesBefore)
                .orElseThrow(() -> new NotInDataBaseException("Не найден актуальный код для переданного номера телефона"));
    }


    //Смотрим сколько раз неверно ввёл код за последние minutesFromNow минут
    @Transactional
    public List<OperationPincode> findAllByOperationNameDirectionAndDateAfter(String phone, int minutesFromNow, String operationName, String direction) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        cal.add(Calendar.MINUTE, -minutesFromNow);
        Date tenMinutesBefore = cal.getTime();
        return operationPinCodeRepository.findAllByLoginUserAndDateAfterAnAndOpRefOperationDirection(phone, operationName, direction, tenMinutesBefore);
    }

    @Transactional
    public List<OperationPincode> findAllByOperationNameDirectionAndDateAfterWithUserPhone(String phone, int minutesFromNow, String operationName, String direction) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        cal.add(Calendar.MINUTE, -minutesFromNow);
        Date tenMinutesBefore = cal.getTime();
        return operationPinCodeRepository.findAllByLoginUserAndDateAfterAnAndOpRefOperationDirection(phone, operationName, direction, tenMinutesBefore);
    }

    @Transactional
    public List<OperationPincode> findAllByOperationNameDirectionAndDateAfterLikeUserLogin(String phone, int minutesFromNow, String operationName, String direction) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        cal.add(Calendar.MINUTE, -minutesFromNow);
        Date tenMinutesBefore = cal.getTime();
        return operationPinCodeRepository.findAllByLikeLoginUserAndDateAfterAnAndOpRefOperationDirection(phone, operationName, direction, tenMinutesBefore);
    }


}
