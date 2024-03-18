package ru.nsu.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOrdersByStatusesResponse {

    private List<AccountOrdersResponse> plannedOrders;

    private List<AccountOrdersResponse> madeOrders;
}
