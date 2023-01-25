package com.example.aluminumquiltphrase.service;

import com.example.aluminumquiltphrase.entity.RepoInfo;
import com.example.aluminumquiltphrase.entity.UserInfo;
import com.example.aluminumquiltphrase.entity.github.GithubRepoInfo;
import com.example.aluminumquiltphrase.entity.github.GithubUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private GithubHttpService githubHttpService;

    public UserService(GithubHttpService githubHttpService) {
        this.githubHttpService = githubHttpService;
    }

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    // Uses RestTemplate to call Github API to retrieve user info and repo info
    public UserInfo getUserInfoByUsername(String username) throws Exception {
        GithubUserInfo githubUserInfo;
        List<GithubRepoInfo> githubRepoInfo;
        try {
            // use the retry template to retry the call if it fails
            githubUserInfo = githubHttpService.getUserByUsername(username);
            githubRepoInfo = githubHttpService.getGithubRepoInfos(githubUserInfo);
        } catch (Exception e) {
            // If the call fails, log it
            logger.error("Error retrieving user info for user: " + username, e);
            throw e;
        }
        UserInfo userInfo = buildUserInfo(githubUserInfo, githubRepoInfo);
        return userInfo;
    }



    private UserInfo buildUserInfo(GithubUserInfo githubUserInfo, List<GithubRepoInfo> githubRepoInfoHolder) {
        // Set all user fields we care about
        UserInfo userInfo = new UserInfo(
                githubUserInfo.getLogin(),
                githubUserInfo.getAvatar_url(),
                githubUserInfo.getEmail(),
                githubUserInfo.getUrl(),
                // Assuming we receive an ISO timestamp, convert the date time to the format we want to display
                githubUserInfo.getCreated_at().replace("T", " ").replace("Z", ""),
                githubUserInfo.getName(),
                githubUserInfo.getLocation()
        );

        // Use a lambda function to map the GithubRepoInfo to a RepoInfo
        userInfo.setRepos(new ArrayList<>());
        githubRepoInfoHolder.forEach(githubRepoInfo -> {
            userInfo.getRepos().add(new RepoInfo(githubRepoInfo.getName(), githubRepoInfo.getUrl()));
        });
        return userInfo;
    }
}
