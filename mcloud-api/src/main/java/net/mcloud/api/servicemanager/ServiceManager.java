package net.mcloud.api.servicemanager;

import net.mcloud.api.servicemanager.services.LobbyInstance;
import net.mcloud.api.servicemanager.services.ProxyInstance;
import net.mcloud.api.servicemanager.services.ServerInstance;
import net.mcloud.api.servicemanager.versions.PaperVersions;
import net.mcloud.api.utils.json.JsonHandler;
import net.mcloud.api.utils.json.ServerStorage;
import net.mcloud.api.utils.json.types.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    private List<ServerInstance> serverInstances;
    private List<ProxyInstance> proxyInstances;
    private List<LobbyInstance> lobbyInstances;

    public ServiceManager() {
        File file = new File(jsonHandler.getStoragePath() + "/" + fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            serverInstances = new ArrayList<>();
            proxyInstances = new ArrayList<>();
            lobbyInstances = new ArrayList<>();
        } else {
            serverInstances = new ArrayList<>();
            proxyInstances = new ArrayList<>();
            lobbyInstances = new ArrayList<>();
            ServerStorage serverStorage;
            try {
                serverStorage = (ServerStorage) jsonHandler.getObject(fileName, ServerStorage.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            serverStorage.servers().forEach(server -> {
                ServerInstance serverInstance = new ServerInstance(server.serverName(), Enum.valueOf(PaperVersions.class, server.version().toUpperCase()));
                serverInstances.add(serverInstance);
            });
        }
    }

    public ServerInstance createServerService(String serverName, PaperVersions serverVersion, int minMemory, int maxMemory) {
        ServerInstance serverInstance = new ServerInstance(serverName, serverVersion);
        serverInstance.setMaxMemory(maxMemory);
        serverInstance.setMinMemory(minMemory);
        serverInstance.createServer();
        serverInstances.add(serverInstance);
        return serverInstance;
    }

    public List<ServerInstance> getServerInstances() {
        return serverInstances;
    }

    public List<ProxyInstance> getProxyInstances() {
        return proxyInstances;
    }

    public List<LobbyInstance> getLobbyInstances() {
        return lobbyInstances;
    }
}
