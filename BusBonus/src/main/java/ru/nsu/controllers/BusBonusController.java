package ru.nsu.controllers;

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

    @Operation(
            summary = "Покупка билетов по BusBonusId билету в базу данных",
            description = "Внешняя система должна по этому адресу отправить запрос со всей возможной информацией о рейсе, билете, маршруте, документах пользователе, " +
                    "а главное - BusBonusId пользователя.",
            tags = {"busBonus", "ticket"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
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
    @Operation(
            summary = "Получение поездок пользователя по BusBonusId",
            description = "Получаем информацию о поездках пользователя. " +
                    "Пока просто тестовый запрос чтобы быстро получать эту самую информацию. " +
                    "Кэшируется 30 минут",
            tags = {"races", "account"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список рейсов аккаунта",
                    content = {@Content(schema = @Schema(implementation = AccountOrdersByStatusesResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/get_account_races_by_busbonus_id/{busBonusId}")
    @Transactional
    public ResponseEntity<?> getAccountRacesByBusBonusId(@PathVariable("busBonusId") String busBonusId) {
        Account account = accountService.getAccountByBBId(busBonusId);
        return ResponseEntity.ok(accountService.getUserOrdersForResponseById(account.getId()));
    }
}
