package com.authmodule;

import jakarta.validation.constraints.NotBlank;

public class AuthorizationRequest {

    @NotBlank(message = "userId must not be blank")
    private String userId;

    @NotBlank(message = "action must not be blank")
    private String action;

    @NotBlank(message = "resource must not be blank")
    private String resource;

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
}