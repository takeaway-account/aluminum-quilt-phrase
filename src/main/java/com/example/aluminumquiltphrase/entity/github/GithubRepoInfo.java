package com.example.aluminumquiltphrase.entity.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubRepoInfo {
    private String name;
    private String url;

    public GithubRepoInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    // Blank constructor for Jackson
    public GithubRepoInfo() {
    }
}
