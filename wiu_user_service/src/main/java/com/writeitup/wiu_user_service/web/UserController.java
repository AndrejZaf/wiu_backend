package com.writeitup.wiu_user_service.web;

import com.writeitup.wiu_user_service.dto.UserDTO;
import com.writeitup.wiu_user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Fetch data for current logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns current user data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<UserDTO> getCurrentUserData() {
        log.info("Received request to retrieve current user data");
        final UserDTO currentUser = userService.getCurrentUser();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}
