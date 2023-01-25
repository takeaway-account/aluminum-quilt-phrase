package com.example.aluminumquiltphrase.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepoInfo {
    private String name;
    private String url;

    public RepoInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }
}