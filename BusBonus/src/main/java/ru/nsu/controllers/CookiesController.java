package ru.nsu.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.payload.response.DataResponse;
import ru.nsu.services.interfaces.ICookiesService;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials = "true")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/cookies")
public class CookiesController {

    private final ICookiesService cookiesService;

    @PostMapping("/accept")
    @Transactional
    public ResponseEntity<?> acceptCookies() {
        long oneYearInSeconds = 60 * 60 * 24 * 365;
        var acceptCookie = cookiesService.generateCookie("hide_banner", "/", "accepted", oneYearInSeconds);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, acceptCookie.toString())
                .body(new DataResponse(true));
    }

}
