package ru.nsu.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.model.user.Account;
import ru.nsu.payload.request.NewTicketsRequest;
import ru.nsu.payload.request.UserTicketByBBId;
import ru.nsu.payload.response.AccountOrdersByStatusesResponse;
import ru.nsu.payload.response.DataResponse;
import ru.nsu.payload.response.MessageResponse;
import ru.nsu.services.interfaces.IAccountService;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/bus_bonus")
public class BusBonusController {

    private final IAccountService accountService;


    @PostMapping("/ticket/new")
    @Transactional
    public ResponseEntity<?> saveNewTicket(@Valid @RequestBody UserTicketByBBId userTicketByBBId) {
        Account accountByBBId = accountService.getAccountByBBId(userTicketByBBId.getBusBonusId());
        accountService.saveNewUserTicketFromExternalSystem(accountByBBId, userTicketByBBId);
        return ResponseEntity.ok(new DataResponse(true));
    }

    @PostMapping("/tickets/new")
    @Transactional
    public ResponseEntity<?> saveSeveralTickets(@Valid @RequestBody NewTicketsRequest newTicketsRequest) {
        if (newTicketsRequest.getUserTicketByBBIdList().size() > 10) {
            return ResponseEntity.badRequest().body(new MessageResponse("За раз нельзя отправлять больше 10 билетов. Пожалуйста, учитывайте это."));
        }
        Account account = accountService.getAccountByBBId(newTicketsRequest.getBusBonusId());
        accountService.saveNewUserTicketsFromExternalSystem(account, newTicketsRequest);
        return ResponseEntity.ok(new DataResponse(true));
    }

    @GetMapping("/get_account_races_by_busbonus_id/{busBonusId}")
    @Transactional
    public ResponseEntity<?> getAccountRacesByBusBonusId(@PathVariable("busBonusId") String busBonusId) {
        Account account = accountService.getAccountByBBId(busBonusId);
        return ResponseEntity.ok(accountService.getUserOrdersForResponseById(account.getId()));
    }
}
