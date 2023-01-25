package com.example.aluminumquiltphrase.entity.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubUserInfo {
    private String login;
    private String name;
    private String avatar_url;
    private String location;
    private String email;
    private String url;
    private String created_at;
    private String repos_url;

    // Blank constructor for Jackson
    public GithubUserInfo() {
    }

    public GithubUserInfo(String login, String name, String avatar_url, String location, String email, String url, String created_at, String repos_url) {
        this.login = login;
        this.name = name;
        this.avatar_url = avatar_url;
        this.location = location;
        this.email = email;
        this.url = url;
        this.created_at = created_at;
        this.repos_url = repos_url;
    }
}
