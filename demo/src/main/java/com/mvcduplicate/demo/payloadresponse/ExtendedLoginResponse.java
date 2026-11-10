package com.mvcduplicate.demo.payloadresponse;

public class ExtendedLoginResponse {

    private Long id;

    private String token;

    public ExtendedLoginResponse(Long id, String token) {
        this.id = id;

        this.token = token;
    }

    public Long getId() { return id; }

    public String getToken() { return token; }
}
