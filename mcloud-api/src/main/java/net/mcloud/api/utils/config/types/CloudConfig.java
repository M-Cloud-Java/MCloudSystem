package net.mcloud.api.utils.config.types;

public record CloudConfig(int tcpPort, int udpPort, boolean deprecatedEvents) {
}
