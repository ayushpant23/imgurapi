package com.ays.imgurapi.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "imgur.client")
public class ImgurData {
    private String clientId;
    private String clientSecret;
    private String authorizeUrl;
    private String accessTokenUrl;
    private String uploadUrl;
    private String albumId;
    private String accessToken;
    private String refreshToken;
    private String images;
    private String albumUrl;
}