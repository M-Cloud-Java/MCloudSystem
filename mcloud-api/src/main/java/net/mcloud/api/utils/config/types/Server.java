package net.mcloud.api.utils.config.types;

public record Server(String serverName, String serverVersion, int minMemory, int maxMemory) { }
