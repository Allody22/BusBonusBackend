package ru.nsu.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountInfoResponse {

    private String phone;

    private String busBonusId;

    private String email;

    private List<UserDataResponse> userDataArray;

    //Добавить ачивки и поездки
}
