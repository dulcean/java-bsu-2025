package com.banking.system.model;

import java.util.UUID;

public class Client {
    private final UUID uuid;
    private final String username;

    public Client(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public Client(String username) {
        this(UUID.randomUUID(), username);
    }

    public UUID getUuid() { return uuid; }
    public String getUsername() { return username; }

    @Override
    public String toString() { return username + " [" + uuid + "]"; }
}