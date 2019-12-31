package br.com.template.rest.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {

    ROLE_ADMIN("admin");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    @JsonValue
    public String getRole() {
        return this.role;
    }

    public static UserRole userRoleWith(String role) {
        if(role == null) { return null; }
        switch (role) {
            case "admin":
                return UserRole.ROLE_ADMIN;
            default:
                return null;
        }
    }

}
