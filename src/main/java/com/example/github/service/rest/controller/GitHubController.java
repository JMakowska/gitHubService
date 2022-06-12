package com.example.github.service.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.github.service.dto.ContributorDTO;
import com.example.github.service.dto.ErrorResponseDTO;
import com.example.github.service.service.GitHubService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @Operation(summary = "Get a list of organization contributors by number of commits")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the contributors"),
            @ApiResponse(responseCode = "401", description = "Incorrect token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403",
                    description = "No token. Number of queries per hour exceeded.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Resource not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
    @GetMapping("/org/{organisation}/contributors")
    List<ContributorDTO> getOrganisationsContributors(
            @Parameter(description = "The organization name. The name is not case sensitive.") @PathVariable String organisation,
            @Parameter(description = "The order to sort by. Default: asc. Can be one of: asc, desc") @RequestParam(required = false, defaultValue = "asc")
            String sortDirection) {
        if ( !sortDirection.equals("asc") && !sortDirection.equals("desc") ) {
            sortDirection = "asc";
        }
        return gitHubService.getSortedContributors(organisation, sortDirection);
    }

}
