package com.example.aluminumquiltphrase.service;

import com.example.aluminumquiltphrase.entity.RepoInfo;
import com.example.aluminumquiltphrase.entity.UserInfo;
import com.example.aluminumquiltphrase.entity.github.GithubRepoInfo;
import com.example.aluminumquiltphrase.entity.github.GithubUserInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {

    @SneakyThrows
    @Test
    void getUserInfoByUsername_correct() {

        // Mock the rest template user call
        GithubUserInfo testGithubUserInfo = new GithubUserInfo(
                "test0",
                "test0-name",
                "https://avatars.githubusercontent.com/u/1?v=4",
                "test0-location",
                "test0-email",
                "https://api.github.com/users/test0",
                "2011-01-26T19:01:12Z",
                "https://api.github.com/users/test0/repos"
        );

        // Mock for the repo call
        GithubRepoInfo[] testGithubRepoInfo = new GithubRepoInfo[]{
                new GithubRepoInfo(
                        "test0-repo0",
                        "https://api.github.com/repos/test0/test0-repo0"),
        };

        GithubHttpService githubHttpService = mock(GithubHttpService.class);

        when(githubHttpService.getUserByUsername(any())).thenReturn(testGithubUserInfo);
        when(githubHttpService.getGithubRepoInfos(any())).thenReturn(List.of(testGithubRepoInfo));

        // Create the service
        UserService userService = new UserService(githubHttpService);

        // Expected Response
        UserInfo expectedUserInfo = new UserInfo(
                "test0",
                "https://avatars.githubusercontent.com/u/1?v=4",
                "test0-email",
                "https://api.github.com/users/test0",
                "2011-01-26 19:01:12",
                "test0-name",
                "test0-location"
        );
        expectedUserInfo.getRepos().add(new RepoInfo("test0-repo0", "https://api.github.com/repos/test0/test0-repo0"));


        // Call the method in the service
        UserInfo userInfo = userService.getUserInfoByUsername("test0");

        // Assert the response
        Assertions.assertEquals(expectedUserInfo.getRepos().get(0).getUrl(), userInfo.getRepos().get(0).getUrl());
        Assertions.assertEquals(expectedUserInfo.getRepos().get(0).getName(), userInfo.getRepos().get(0).getName());
        Assertions.assertEquals(expectedUserInfo.getAvatar(), userInfo.getAvatar());
        Assertions.assertEquals(expectedUserInfo.getEmail(), userInfo.getEmail());

    }

    @SneakyThrows
    @Test
    void getUserInfoByUsername_noRepos() {

        // Mock the rest template user call
        GithubUserInfo testGithubUserInfo = new GithubUserInfo(
                "test0",
                "test0-name",
                "https://avatars.githubusercontent.com/u/1?v=4",
                "test0-location",
                "test0-email",
                "https://api.github.com/users/test0",
                "2011-01-26T19:01:12Z",
                "https://api.github.com/users/test0/repos"
        );

        // Mock for the repo call
        GithubRepoInfo[] testGithubRepoInfo = new GithubRepoInfo[]{};

        GithubHttpService githubHttpService = mock(GithubHttpService.class);

        when(githubHttpService.getUserByUsername(any())).thenReturn(testGithubUserInfo);
        when(githubHttpService.getGithubRepoInfos(any())).thenReturn(List.of(testGithubRepoInfo));

        // Create the service
        UserService userService = new UserService(githubHttpService);

        // Expected Response
        UserInfo expectedUserInfo = new UserInfo(
                "test0",
                "https://avatars.githubusercontent.com/u/1?v=4",
                "test0-email",
                "https://api.github.com/users/test0",
                "2011-01-26 19:01:12",
                "test0-name",
                "test0-location"
        );


        // Call the method in the service
        UserInfo userInfo = userService.getUserInfoByUsername("test0");

        // Assert the response
        Assertions.assertEquals(expectedUserInfo.getRepos(), userInfo.getRepos());
        Assertions.assertEquals(expectedUserInfo.getAvatar(), userInfo.getAvatar());
        Assertions.assertEquals(expectedUserInfo.getEmail(), userInfo.getEmail());

    }

}