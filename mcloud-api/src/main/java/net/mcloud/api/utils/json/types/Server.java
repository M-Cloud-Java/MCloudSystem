package net.mcloud.api.utils.json.types;

public record Server(String serverName, String version, int minMemory, int maxMemory) {
}
