package com.authmodule;

public class AuthorizationResponse {
    private boolean authorized;

    public AuthorizationResponse(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
