package net.mcloud.api.servicemanager.services;

import net.mcloud.api.MCloudAPI;
import net.mcloud.api.servicemanager.versions.PaperVersions;
import net.mcloud.api.utils.Downloader;
import net.mcloud.api.utils.logger.ConsoleColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerInstance extends Instance {

    private File serverFolder;
    private File pluginFolder;
    private File startServerFile;

    public ServerInstance(String serverName, PaperVersions paperVersion) {
        super(serverName, paperVersion);
    }

    @Override
    public boolean createServer() {
        serverFolder = new File("services/" + "serverName");
        String minMemoryString = "-Xms" + getMinMemory() + "M";
        String maxMemoryString = "-Xmx" + getMaxMemory() + "M";
        if (!serverFolder.exists())
            serverFolder.mkdirs();
        else
            MCloudAPI.getApi().getLogger().info("Please choose a other Name for this Server because the Service is already defined!", ConsoleColor.RED);

        pluginFolder = new File("services/" + getServerName() + "/plugins");
        if (!pluginFolder.exists())
            pluginFolder.mkdirs();

        switch (getOS()) {
            case LINUX -> {
                startServerFile = new File("services/" + "serverName" + "/start.sh");
                try {
                    startServerFile.createNewFile();
                    FileWriter writer = new FileWriter(startServerFile);
                    writer.write("java -server -Dcom.mojang.eula.agree=true " + minMemoryString + " " + maxMemoryString + " -jar paper-server.jar nogui");
                    writer.close();
                    Runtime.getRuntime().exec("chmod u+x " + startServerFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case WINDOWS -> {
                startServerFile = new File("services/" + getServerName() + "/start.bat");
                try {
                    startServerFile.createNewFile();
                    FileWriter writer = new FileWriter(startServerFile);
                    writer.write("java -server -Dcom.mojang.eula.agree=true " + minMemoryString + " " + maxMemoryString + " -jar paper-server.jar nogui");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case MACOS -> {
                startServerFile = new File("services/" + getServerName() + "/start.sh");
                try {
                    startServerFile.createNewFile();
                    FileWriter writer = new FileWriter(startServerFile);
                    writer.write("java -server -Dcom.mojang.eula.agree=true " + minMemoryString + " " + maxMemoryString + " -jar paper-server.jar nogui");
                    writer.close();
                    Runtime.getRuntime().exec("chmod u+x " + startServerFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            default -> {
                MCloudAPI.getApi().getLogger().error("The Cloud can not start the Server because you use a unknown Os System");
            }
        }

        Downloader downloader;
        try {
            downloader = new Downloader(new URL(getPaperVersion().getDownloadUrl()), "services/" + getServerName() + "/paper-server.jar");
            downloader.downloadFile();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        while (!downloader.isFinished()) {
            MCloudAPI.getApi().getLogger().info("The Downloader is not Finished!", ConsoleColor.GREEN);
        }

        MCloudAPI.getApi().getLogger().info("The Downloader is Finished!", ConsoleColor.RED);
        try {
            startServer();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean startServer() throws IOException {
        Process process;
        ProcessBuilder processBuilder = new ProcessBuilder();
        switch (getOS()) {
            case LINUX, MACOS -> {
                processBuilder = new ProcessBuilder();
                processBuilder.directory(new File("services/" + getServerName() + "/"));
                processBuilder.command("./start.sh");
            }
            case WINDOWS -> {
                processBuilder = new ProcessBuilder();
                processBuilder.directory(new File("services/" + getServerName() + "/"));
                processBuilder.command("cmd", "/c", "start.bat");
            }
        }
        process = processBuilder.start();

        if (process.isAlive()) {
            MCloudAPI.getApi().getLogger().info("Server: " + getServerName() + " is Alive", ConsoleColor.CYAN);
            return true;
        }
        MCloudAPI.getApi().getLogger().info("Server: " + getServerName() + " is not Alive", ConsoleColor.CYAN);
        return false;
    }

    public File getPluginFolder() {
        return pluginFolder;
    }

    public File getServerFolder() {
        return serverFolder;
    }

    public File getStartServerFile() {
        return startServerFile;
    }
}
