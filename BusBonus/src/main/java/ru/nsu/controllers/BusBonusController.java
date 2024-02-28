package ru.nsu.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.model.user.Account;
import ru.nsu.payload.request.UserTicketByBBId;
import ru.nsu.payload.response.DataResponse;
import ru.nsu.services.AccountService;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/v1/bus_bonus")
public class BusBonusController {

    private final AccountService accountService;

    @Autowired
    public BusBonusController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/ticket/new")
    @Transactional
    public ResponseEntity<?> saveNewTicket(@Valid @RequestBody UserTicketByBBId userTicketByBBId) {
        Account accountByBBId = accountService.getAccountByBBId(userTicketByBBId.getBusBonusId());
        accountService.saveNewUserTicketFromExternalSystem(accountByBBId, userTicketByBBId);
        return ResponseEntity.ok(new DataResponse(true));
    }
}
