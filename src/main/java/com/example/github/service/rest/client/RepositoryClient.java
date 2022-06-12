package com.example.github.service.rest.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.github.service.dto.ContributorDTO;

@FeignClient(name = "repositoryClient", url = "${github.url}")
public interface RepositoryClient {

    @GetMapping(value = "/repos/{owner}/{repository}/contributors", produces = "application/json")
    List<ContributorDTO> getRepositoryContributors(@PathVariable("owner") String owner, @PathVariable("repository") String repository,
            @RequestParam("per_page") int perPage, @RequestParam("page") int page);
}
