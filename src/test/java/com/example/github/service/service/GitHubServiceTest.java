package com.example.github.service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.github.service.dto.ContributorDTO;
import com.example.github.service.dto.RepositoryDTO;
import com.example.github.service.dto.RepositoryOwnerDTO;
import com.example.github.service.rest.client.OrganizationClient;
import com.example.github.service.rest.client.RepositoryClient;

class GitHubServiceTest {

    private final RepositoryClient repositoryClient = mock(RepositoryClient.class);

    private final OrganizationClient organizationClient = mock(OrganizationClient.class);

    private final GitHubService gitHubService = new GitHubService(organizationClient, repositoryClient);

    @Test
    void shouldReturnEmptyContributorsList() {
        // GIVEN
        List<RepositoryDTO> repositories = new ArrayList<>();
        RepositoryOwnerDTO repositoryOwner = new RepositoryOwnerDTO("owner");
        RepositoryDTO repository1 = new RepositoryDTO("repository1", repositoryOwner);
        RepositoryDTO repository2 = new RepositoryDTO("repository2", repositoryOwner);
        repositories.add(repository1);
        repositories.add(repository2);

        // WHEN
        when(organizationClient.getOrganizationRepositories(anyString(), anyInt(), anyInt())).thenReturn(repositories);
        when(repositoryClient.getRepositoryContributors(anyString(), anyString(), anyInt(), anyInt())).thenReturn(null);
        List<ContributorDTO> contributors = gitHubService.getSortedContributors("organization", "asc");

        // THEN
        assertEquals(contributors, new ArrayList<>());
    }

    @Test
    void shouldReturnSortedContributorsAsc() {
        // GIVEN
        List<RepositoryDTO> repositories = new ArrayList<>();
        RepositoryOwnerDTO repositoryOwner = new RepositoryOwnerDTO("owner");
        RepositoryDTO repository1 = new RepositoryDTO("repository1", repositoryOwner);
        RepositoryDTO repository2 = new RepositoryDTO("repository2", repositoryOwner);
        repositories.add(repository1);
        repositories.add(repository2);

        ContributorDTO contributor1ForRepo1 = new ContributorDTO("contributor1", 15);
        ContributorDTO contributor1ForRepo2 = new ContributorDTO("contributor2", 25);
        ContributorDTO contributor2ForRepo2 = new ContributorDTO("contributor3", 21);
        List<ContributorDTO> contributorsForRepo1 = new ArrayList<>();
        contributorsForRepo1.add(contributor1ForRepo1);
        List<ContributorDTO> contributorsForRepo2 = new ArrayList<>();
        contributorsForRepo2.add(contributor1ForRepo2);
        contributorsForRepo2.add(contributor2ForRepo2);

        List<ContributorDTO> expectedResult = new ArrayList<>();
        expectedResult.add(contributor1ForRepo1);
        expectedResult.add(contributor2ForRepo2);
        expectedResult.add(contributor1ForRepo2);

        // WHEN
        when(organizationClient.getOrganizationRepositories(anyString(), anyInt(), anyInt())).thenReturn(repositories);
        when(repositoryClient.getRepositoryContributors(anyString(), eq("repository1"), anyInt(), anyInt())).thenReturn(contributorsForRepo1);
        when(repositoryClient.getRepositoryContributors(anyString(), eq("repository2"), anyInt(), anyInt())).thenReturn(contributorsForRepo2);
        List<ContributorDTO> contributors = gitHubService.getSortedContributors("organization", "asc");

        // THEN
        assertEquals(contributors, expectedResult);
    }

    @Test
    void shouldReturnSortedContributorsDesc() {
        // GIVEN
        List<RepositoryDTO> repositories = new ArrayList<>();
        RepositoryOwnerDTO repositoryOwner = new RepositoryOwnerDTO("owner");
        RepositoryDTO repository1 = new RepositoryDTO("repository1", repositoryOwner);
        RepositoryDTO repository2 = new RepositoryDTO("repository2", repositoryOwner);
        repositories.add(repository1);
        repositories.add(repository2);

        ContributorDTO contributor1ForRepo1 = new ContributorDTO("contributor1", 15);
        ContributorDTO contributor2ForRepo1 = new ContributorDTO("contributor3", 51);
        ContributorDTO contributor1ForRepo2 = new ContributorDTO("contributor2", 25);
        ContributorDTO contributor2ForRepo2 = new ContributorDTO("contributor3", 21);
        ContributorDTO contributor3ForAllRepos = new ContributorDTO("contributor3", 72);

        List<ContributorDTO> contributorsForRepo1 = new ArrayList<>();
        contributorsForRepo1.add(contributor1ForRepo1);
        contributorsForRepo1.add(contributor2ForRepo1);
        List<ContributorDTO> contributorsForRepo2 = new ArrayList<>();
        contributorsForRepo2.add(contributor1ForRepo2);
        contributorsForRepo2.add(contributor2ForRepo2);

        List<ContributorDTO> expectedResult = new ArrayList<>();
        expectedResult.add(contributor3ForAllRepos);
        expectedResult.add(contributor1ForRepo2);
        expectedResult.add(contributor1ForRepo1);

        // WHEN
        when(organizationClient.getOrganizationRepositories(anyString(), anyInt(), anyInt())).thenReturn(repositories);
        when(repositoryClient.getRepositoryContributors(anyString(), eq("repository1"), anyInt(), anyInt())).thenReturn(contributorsForRepo1);
        when(repositoryClient.getRepositoryContributors(anyString(), eq("repository2"), anyInt(), anyInt())).thenReturn(contributorsForRepo2);
        List<ContributorDTO> contributors = gitHubService.getSortedContributors("organization", "desc");

        // THEN
        assertEquals(contributors, expectedResult);
    }
}
