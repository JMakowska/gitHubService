package com.example.github.service.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;

import com.example.github.service.dto.ContributorDTO;
import com.example.github.service.dto.RepositoryDTO;
import com.example.github.service.rest.client.OrganizationClient;
import com.example.github.service.rest.client.RepositoryClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private static final int PER_PAGE = 100;

    private final OrganizationClient organizationClient;

    private final RepositoryClient repositoryClient;

    public List<ContributorDTO> getSortedContributors(String organization, String sortDirection) {
        List<RepositoryDTO> repositories = getAllRepositoriesForOrganization(organization);
        Map<String, Integer> allContributors = getContributorsForAllRepositories(repositories);

        return allContributors.entrySet()
                .stream()
                .map(c -> new ContributorDTO(c.getKey(), c.getValue()))
                .sorted(sortDirection.equals("asc") ?
                        Comparator.comparing(ContributorDTO::getContributions) :
                        Comparator.comparing(ContributorDTO::getContributions)
                                .reversed())
                .toList();
    }

    @VisibleForTesting
    private List<RepositoryDTO> getAllRepositoriesForOrganization(String organization) {
        List<RepositoryDTO> repositories = new ArrayList<>();
        List<RepositoryDTO> repositoriesResponse;
        int pageNumber = 1;
        do {
            repositoriesResponse = organizationClient.getOrganizationRepositories(organization, PER_PAGE, pageNumber);
            Optional.ofNullable(repositoriesResponse)
                    .ifPresent(repositories::addAll);
            pageNumber++;
        } while (!shouldStopRequesting(repositoriesResponse));
        return repositories;
    }

    @VisibleForTesting
    private Map<String, Integer> getContributorsForAllRepositories(List<RepositoryDTO> repositories) {
        Map<String, Integer> allContributors = new HashMap<>();
        repositories.forEach(repository -> {
            List<ContributorDTO> contributors = getContributorsForRepository(repository.getRepositoryOwnerDTO()
                    .getLogin(), repository.getName());
            contributors.forEach(contributor -> allContributors.merge(contributor.getLogin(), contributor.getContributions(), Integer::sum));
        });
        return allContributors;
    }

    @VisibleForTesting
    private List<ContributorDTO> getContributorsForRepository(String owner, String repository) {
        List<ContributorDTO> contributors = new ArrayList<>();
        List<ContributorDTO> contributorResponse;
        int pageNumber = 1;
        do {
            contributorResponse = repositoryClient.getRepositoryContributors(owner, repository, PER_PAGE, pageNumber);
            Optional.ofNullable(contributorResponse)
                    .ifPresent(contributors::addAll);
            pageNumber++;
        } while (!shouldStopRequesting(contributorResponse));
        return contributors;
    }

    private <T> boolean shouldStopRequesting(List<T> list) {
        return list == null || list.size() == 0 || list.size() < PER_PAGE;
    }

}
