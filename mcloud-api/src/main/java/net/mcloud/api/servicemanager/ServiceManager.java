package net.mcloud.api.servicemanager;

import net.mcloud.api.MCloudAPI;
import net.mcloud.api.servicemanager.services.LobbyInstance;
import net.mcloud.api.servicemanager.services.ProxyInstance;
import net.mcloud.api.servicemanager.services.ServerInstance;
import net.mcloud.api.servicemanager.versions.PaperVersions;
import net.mcloud.api.utils.config.ConfigType;
import net.mcloud.api.utils.config.XmlConfigFile;
import net.mcloud.api.utils.config.types.Server;
import net.mcloud.api.utils.logger.ConsoleColor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    private List<ServerInstance> serverInstances;
    private List<ProxyInstance> proxyInstances;
    private List<LobbyInstance> lobbyInstances;

    private File dirServer;
    private File dirProxy;
    private File dirLobby;
    public ServiceManager() {
           dirServer = new File("storage/server");
           if(!dirServer.exists())
               dirServer.mkdirs();
           if(dirServer.isDirectory()) {
               if(dirServer.listFiles() == null) {
                    serverInstances = new ArrayList<>();
               } else {
                   serverInstances = new ArrayList<>();
                   List<File> fileList = List.of(dirServer.listFiles());
                   fileList.forEach(file -> {
                       XmlConfigFile xmlConfigFile = new XmlConfigFile(dirServer.getPath(), file.getName());
                       Server server = (Server) xmlConfigFile.loadFile(ConfigType.SERVER);
                       ServerInstance serverInstance = new ServerInstance(server.serverName(), Enum.valueOf(PaperVersions.class, server.serverVersion()));
                       serverInstance.setMinMemory(server.minMemory());
                       serverInstance.setMaxMemory(server.maxMemory());
                       MCloudAPI.getApi().getLogger().info("The ServiceManager has added a Server: " + serverInstance.getServerName(), ConsoleColor.CYAN);
                       serverInstances.add(serverInstance);
                   });
               }
           }
    }

    public void saveServices() {
        dirServer = new File("storage/server");
        if(!dirServer.exists())
            dirServer.mkdirs();
        if(dirServer.isDirectory()) {
            if(dirServer.listFiles() == null) {
                serverInstances.forEach(serverInstance -> {
                    XmlConfigFile xmlConfigFile = new XmlConfigFile(dirServer.getPath(), serverInstance.getServerName() + ".xml");
                    xmlConfigFile.createFile(new Server(serverInstance.getServerName(), serverInstance.getPaperVersion().name(),
                            serverInstance.getMinMemory(), serverInstance.getMaxMemory()));
                });
            } else {
                List<File> files = List.of(dirServer.listFiles());
                files.forEach(file -> {
                    serverInstances.forEach(serverInstance -> {
                        if(file.getName().equals(serverInstance.getServerName() + ".xml")) {
                            XmlConfigFile xmlConfigFile = new XmlConfigFile(dirServer.getPath(), file.getName());
                            xmlConfigFile.updateFile(new Server(serverInstance.getServerName(), serverInstance.getPaperVersion().name(),
                                    serverInstance.getMinMemory(), serverInstance.getMaxMemory()));
                            MCloudAPI.getApi().getLogger().info("The Config Server File: " + file.getName() + " is updated successfully", ConsoleColor.PURPLE);
                        } else {
                            XmlConfigFile xmlConfigFile = new XmlConfigFile(dirServer.getPath(), serverInstance.getServerName() + ".xml");
                            xmlConfigFile.createFile(new Server(serverInstance.getServerName(), serverInstance.getPaperVersion().name(),
                                    serverInstance.getMinMemory(), serverInstance.getMaxMemory()));
                            MCloudAPI.getApi().getLogger().info("The Config Server File: " + xmlConfigFile.getFileName() + " is successfully created", ConsoleColor.PURPLE);
                        }
                    });
                });
            }
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
