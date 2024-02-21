package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sale {
    private String lastName;
    private String firstName;
    private String middleName;
    private String docNum;
    private String citizenship;
    private String birthday;
    private String phone;
    private String email;
    private String seatCode;
    private String ticketTypeCode;
}
