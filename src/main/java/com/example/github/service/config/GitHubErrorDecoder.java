package com.example.github.service.config;

import java.io.IOException;
import java.io.InputStream;

import com.example.github.service.dto.GitHubErrorMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class GitHubErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        GitHubErrorMessageDTO message;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, GitHubErrorMessageDTO.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        FeignException exception = FeignException.errorStatus(methodKey, response);
        return switch (response.status()) {
            case 401 -> new FeignException.Unauthorized(message.getMessage(), exception.request(), exception.request()
                    .body(), exception.request()
                    .headers());
            case 403 -> new FeignException.Forbidden(message.getMessage(), exception.request(), exception.request()
                    .body(), exception.request()
                    .headers());
            case 404 -> new FeignException.NotFound(message.getMessage(), exception.request(), exception.request()
                    .body(), exception.request()
                    .headers());
            default -> exception;
        };
    }
}
