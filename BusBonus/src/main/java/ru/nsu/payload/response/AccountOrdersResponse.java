package ru.nsu.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOrdersResponse {

    private List<AccountTripsResponse> accountTripsResponses;

    private int baggageTicketsNumber = 0;

    private int passengerTicketsNumber = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date bookingTime;

    private String arrivalPoint;

    private String arrivalAddress;

    private String dispatchAddress;

    private String carrierName;

    private String carrierINN;

    private String raceName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dispatchDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime arrivalDate;

}
