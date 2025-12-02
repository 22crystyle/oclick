package org.oclick.shell.support.model;

import lombok.Getter;

@Getter
public enum RestKeys {
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    GRANT_TYPE("grant_type");

    private final String key;

    RestKeys(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
