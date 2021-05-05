package com.mostafabor3e.messenger.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String token;
    private String profile_image;

    public User() {
    }

    public User(String name, String profile_image) {
        this.name = name;
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
