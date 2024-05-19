package ru.nsu.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.configuration.security.jwt.JwtUtils;
import ru.nsu.exceptions.FingerPrintException;
import ru.nsu.model.RefreshToken;
import ru.nsu.payload.request.AuthRequest;
import ru.nsu.payload.request.FingerPrintRequest;
import ru.nsu.payload.response.DataResponse;
import ru.nsu.payload.response.JwtResponse;
import ru.nsu.payload.response.MessageResponse;
import ru.nsu.services.RefreshTokenService;
import ru.nsu.services.UserDetailsImpl;
import ru.nsu.services.interfaces.IAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final IAccountService accountService;

    @Transactional
    public ResponseEntity<?> closeAllSessionsForce(HttpServletRequest request) {
        String refreshTokenFromCookies = refreshTokenService.getJwtRefreshFromCookies(request);
        RefreshToken refreshToken = refreshTokenService.findByRefreshToken(refreshTokenFromCookies);

        refreshTokenService.deleteAllRefreshTokenByAccountId(refreshToken.getAccount().getId());
        ResponseCookie jwtRefreshCookie = refreshTokenService.deleteRefreshJwtCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new DataResponse(true));
    }

    @PostMapping("/auth/logout")
    @Transactional
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            // Пытаемся получить refresh token из cookies
            refreshTokenService.processLogout(request);
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
        }
        ResponseCookie jwtRefreshCookie = refreshTokenService.deleteRefreshJwtCookie();


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new DataResponse(true));
    }


    @PostMapping("/auth/refresh")
    @Transactional
    public ResponseEntity<?> refreshTokenPost(HttpServletRequest request, @RequestBody @Valid FingerPrintRequest fingerPrintRequest) {
        String refreshToken = refreshTokenService.getJwtRefreshFromCookies(request);
        String newFingerPrint = fingerPrintRequest.getFingerPrint();

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            RefreshToken foundedToken = refreshTokenService.findByTokenInCache(refreshToken);
            if (!foundedToken.getFingerPrint().equals(newFingerPrint)) {
                throw new FingerPrintException(newFingerPrint, "Фингерпринты не совпадают");
            }
            refreshTokenService.verifyExpiration(foundedToken);

            Long accountId = foundedToken.getAccount().getId();
            String accountPhone = foundedToken.getAccount().getPhone();
            if (refreshTokenService.findAllByAccountId(accountId).size() >= 5) {
                return ResponseEntity.badRequest().body(new MessageResponse("Уже и так создано много сессий!"));
            }

            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(accountId, newFingerPrint);

            refreshTokenService.deleteRefreshToken(foundedToken);
            ResponseCookie jwtRefreshCookie = refreshTokenService.generateRefreshJwtCookie(newRefreshToken.getToken());

            JwtResponse jwtResponse = jwtUtils.generateJwtToken(accountPhone);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(jwtResponse);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }


    @PostMapping("/auth")
    @Transactional
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {

        String userPhone = authRequest.getLogin();

        if (!accountService.checkAccExistenceByPhone(userPhone)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Пожалуйста, проверьте введённые данные."));
        }
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userPhone, authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            JwtResponse jwtResponse = jwtUtils.generateJwtToken(userDetails.getUsername());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), authRequest.getFingerPrint());

            ResponseCookie jwtRefreshCookie = refreshTokenService.generateRefreshJwtCookie(refreshToken.getToken());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(jwtResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Пожалуйста, проверьте введённые данные."));
        }
    }
}
