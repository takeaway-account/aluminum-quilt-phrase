package com.example.aluminumquiltphrase.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@JsonPropertyOrder({"user_name", "display_name", "avatar", "geo_location", "email", "url", "created_at", "repos"})
public class UserInfo {
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("geo_location")
    private String geoLocation;
    @JsonProperty("email")
    private String email;
    @JsonProperty("url")
    private String url;
    @JsonProperty("created_at")
    private String createdAt;
    ArrayList<RepoInfo> repos = new ArrayList<RepoInfo>();



    public UserInfo(String login, String avatarUrl, String email, String url, String createdAt, String name, String location) {
        this.userName = login;
        this.avatar = avatarUrl;
        this.email = email;
        this.url = url;
        this.createdAt = createdAt;
        this.displayName = name;
        this.geoLocation = location;
    }
}
