package com.sinosoft.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePathToLocalService {
    @Value("${basic.profile}")
    private String profile;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String localPath(String file) {
        System.out.println(profile);
        return file.replace("{storage.path.url}/profile", profile);
    }
}
