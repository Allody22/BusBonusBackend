package ru.nsu.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.payload.response.DocumentsTypesResponse;
import ru.nsu.payload.response.TicketCategoriesResponse;
import ru.nsu.services.AccountService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/constants")
public class ConstantsController {

    private final AccountService accountService;

    @Autowired
    public ConstantsController(
            AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/get/documents_types")
    @Transactional
    public ResponseEntity<?> getAllDocumentTypes() {
        return ResponseEntity.ok(new DocumentsTypesResponse(accountService.getAllDocumentTypes()));
    }

    @GetMapping("/get/ticket_categories")
    @Transactional
    public ResponseEntity<?> getAllTickerCategories() {
        return ResponseEntity.ok(new TicketCategoriesResponse(accountService.getAllTicketCategories()));
    }
}
