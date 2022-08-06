package net.mcloud.api.utils.config;

public record CloudConfig(int tcpPort, int udpPort, boolean deprecatedEvents) {
}
