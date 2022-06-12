package com.example.github.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubErrorMessageDTO {
    private String message;
    @JsonProperty("documentation_url")
    private String documentationUrl;
}
