package com.example.github.service.rest.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.github.service.dto.RepositoryDTO;

@FeignClient(name = "organizationClient", url = "${github.url}")
public interface OrganizationClient {

    @GetMapping(value = "/orgs/{organization}/repos", produces = "application/json")
    List<RepositoryDTO> getOrganizationRepositories(@PathVariable("organization") String organization, @RequestParam("per_page") int perPage,
            @RequestParam("page") int page);
}
