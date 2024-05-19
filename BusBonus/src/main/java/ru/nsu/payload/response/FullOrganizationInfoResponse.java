package ru.nsu.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class FullOrganizationInfoResponse {

    private String organizationName;

    private String organizationInn;

    private List<CredentialsResponse> organizationCredentials = new ArrayList<>();
}
