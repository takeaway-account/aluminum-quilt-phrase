package com.example.aluminumquiltphrase.entity.github;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
// Used to hold repos in a list object (from Github's API)
public class GithubRepoInfoHolder {
    private ArrayList<GithubRepoInfo> githubRepoInfos;
}
