package ru.nsu.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocumentsTypesResponse {

    private List<String> types;
}
