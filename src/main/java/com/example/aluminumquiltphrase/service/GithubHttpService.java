package com.example.aluminumquiltphrase.service;

import com.example.aluminumquiltphrase.entity.github.GithubRepoInfo;
import com.example.aluminumquiltphrase.entity.github.GithubUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GithubHttpService {
    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public List<GithubRepoInfo> getGithubRepoInfos(GithubUserInfo githubUserInfo) {
        return List.of(retryTemplate.execute(context -> restTemplate.getForObject(
                githubUserInfo.getRepos_url(), GithubRepoInfo[].class)));
    }

    public GithubUserInfo getUserByUsername(String username) {
        return retryTemplate.execute(context -> restTemplate.getForObject(
                "https://api.github.com/users/" + username, GithubUserInfo.class));
    }
}
