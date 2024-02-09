package ru.nsu.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TicketCategoriesResponse {

    private List<String> categories;
}
