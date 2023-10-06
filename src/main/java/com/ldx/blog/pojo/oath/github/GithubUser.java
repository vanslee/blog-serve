package com.ldx.blog.pojo.oath.github;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Uaena
 * @date 2023/6/10 2:10
 */
@Getter
@Setter
@ToString
public class GithubUser {
    private String login;
    private Long id;
    private String nodeId;
    private String avatarUrl;
    private String gravatarId;
    private String url;
    private String htmlUrl;
    private String followersUrl;
    private String followingUrl;
    private String gistsUrl;
    private String starredUrl;
    private String subscriptionsUrl;
    private String organizationsUrl;
    private String reposUrl;
    private String eventsUrl;
    private String receivedEventsUrl;
    private String type;
    private boolean siteAdmin;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private boolean hireable;
    private String bio;
    private String twitterUsername;
    private int publicRepos;
    private int publicGists;
    private int followers;
    private int following;
    private String createdAt;
    private String updatedAt;
    private int privateGists;
    private int totalPrivateRepos;
    private int ownedPrivateRepos;
    private int diskUsage;
    private int collaborators;
    private boolean twoFactorAuthentication;

}
