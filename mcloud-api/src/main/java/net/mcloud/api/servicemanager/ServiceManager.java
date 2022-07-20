package net.mcloud.api.servicemanager;

import net.mcloud.api.servicemanager.services.LobbyInstance;
import net.mcloud.api.servicemanager.services.ProxyInstance;
import net.mcloud.api.servicemanager.services.ServerInstance;
import net.mcloud.api.servicemanager.versions.PaperVersions;

import java.util.ArrayList;

public class ServiceManager {

    private final ArrayList<ServerInstance> serverInstances;
    private final ArrayList<ProxyInstance> proxyInstances;
    private final ArrayList<LobbyInstance> lobbyInstances;

    public ServiceManager() {
        serverInstances = new ArrayList<>();
        proxyInstances = new ArrayList<>();
        lobbyInstances = new ArrayList<>();
    }

    public ServerInstance createServerService(String serverName, PaperVersions serverVersion) {
        ServerInstance serverInstance = new ServerInstance(serverName, serverVersion);
        serverInstances.add(serverInstance);
        return serverInstance;
    }
}
