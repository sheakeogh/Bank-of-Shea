package com.bank.backend.controller;

import com.bank.backend.model.AuthenticationResponse;
import com.bank.backend.model.User;
import com.bank.backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register New User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New User Creation", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> register(@RequestBody User request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "User Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Login", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> login(@RequestBody User request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request));
    }

    @GetMapping("/refresh_token")
    @Operation(summary = "Refresh User Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh User Token", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.refreshToken(request, response));
    }
}