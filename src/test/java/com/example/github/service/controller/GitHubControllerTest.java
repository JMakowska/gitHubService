package com.example.github.service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.example.github.service.GitHubServiceApplication;
import com.example.github.service.dto.ContributorDTO;
import com.example.github.service.service.GitHubService;

@SpringBootTest(classes = GitHubServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubControllerTest {

    @MockBean
    private GitHubService gitHubService;

    @LocalServerPort
    private int portNumber;

    @Test
    void getContributors_isOk() throws Exception {
        // GIVEN
        String organization = "organization";
        List<ContributorDTO> contributors = new ArrayList<>();
        contributors.add(new ContributorDTO("contributor1", 15));

        HttpUriRequest httpUriRequest = new HttpGet("http://localhost:" + portNumber + "/org/" + organization + "/contributors");

        // WHEN
        when(gitHubService.getSortedContributors(organization, "asc")).thenReturn(contributors);
        HttpResponse httpResponse = HttpClientBuilder.create()
                .build()
                .execute(httpUriRequest);

        // THEN
        assertEquals(httpResponse.getStatusLine()
                .getStatusCode(), HttpStatus.OK.value());
    }

}
