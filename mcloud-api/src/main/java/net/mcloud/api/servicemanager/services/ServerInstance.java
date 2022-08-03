package net.mcloud.api.servicemanager.services;

import net.mcloud.api.MCloudApi;
import net.mcloud.api.servicemanager.versions.PaperVersions;
import net.mcloud.api.utils.Downloader;
import net.mcloud.api.utils.json.types.Server;
import net.mcloud.api.utils.logger.ConsoleColor;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class ServerInstance {

    private String serverName;
    private File serverFolder;
    private File pluginFolder;
    private File startServerFile;

    private PaperVersions paperVersion;
    private int minMemory;
    private int maxMemory;

    public ServerInstance(String serverName, PaperVersions serverVersion, int minMemory, int maxMemory) {
        this.serverName = serverName;
        this.paperVersion  = serverVersion;
        this.minMemory = minMemory;
        this.maxMemory = maxMemory;
    }

    public void createServer() {
        serverFolder = new File("services/" + serverName);
        String minMemoryString = "-Xms" + minMemory + "M";
        String maxMemoryString = "-Xmx" + maxMemory + "M";
        if(!serverFolder.exists())
            serverFolder.mkdirs();
        else
            MCloudApi.getApi().getLogger().info("Please choose a other Name for this Server because the Service is already defined!", ConsoleColor.RED);

        pluginFolder = new File("services/" + serverName + "/plugins");
        if(!pluginFolder.exists())
            pluginFolder.mkdirs();

        String osName = System.getProperty("os.name");
        if(osName.equals("Linux")) {
            startServerFile = new File("services/" + serverName + "/start.sh");
            try {
                startServerFile.createNewFile();
                FileWriter writer = new FileWriter(startServerFile);
                writer.write("java -server -Dcom.mojang.eula.agree=true " + minMemoryString + " " + maxMemoryString + " -jar paper-server.jar nogui");
                writer.close();
                Runtime.getRuntime().exec("chmod u+x " + startServerFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(osName.contains("Windows")) {
            startServerFile = new File("services/" + serverName + "/start.bat");
            try {
                startServerFile.createNewFile();
                FileWriter writer = new FileWriter(startServerFile);
                writer.write("java -server -Dcom.mojang.eula.agree=true " + minMemoryString + " " + maxMemoryString + " -jar paper-server.jar nogui");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(osName.equals("macOS")) {
            startServerFile = new File("services/" + serverName + "/start.sh");
            try {
                startServerFile.createNewFile();
                FileWriter writer = new FileWriter(startServerFile);
                writer.write("java -server -Dcom.mojang.eula.agree=true " + minMemoryString + " " + maxMemoryString + " -jar paper-server.jar nogui");
                writer.close();
                Runtime.getRuntime().exec("chmod u+x " + startServerFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MCloudApi.getApi().getLogger().error("The Cloud can not start the Server because you use a unknown Os System");
        }
        Downloader downloader;
        try {
            downloader = new Downloader(new URL(paperVersion.getDownloadUrl()), "services/" + serverName + "/paper-server.jar");
            downloader.downloadFile();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        while (!downloader.isFinished()) {
            MCloudApi.getApi().getLogger().info("The Downloader is not Finished!", ConsoleColor.GREEN);
        }

        MCloudApi.getApi().getLogger().info("The Downloader is Finished!", ConsoleColor.GREEN);
        try {
            startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void startServer() throws IOException {
        Process process;
        ProcessBuilder processBuilder = new ProcessBuilder();
        String osName = System.getProperty("os.name");
        if(osName.equals("Linux")) {
            processBuilder = new ProcessBuilder();
            processBuilder.directory(new File("services/" + serverName + "/"));
            processBuilder.command("./start.sh");
        }
        if(osName.contains("Windows")) {
            processBuilder = new ProcessBuilder();
            processBuilder.directory(new File("services/" + serverName + "/"));
            processBuilder.command("cmd", "/c", "start.bat");
        }
        if(osName.equals("macOS")) {
            processBuilder = new ProcessBuilder();
            processBuilder.directory(new File("services/" + serverName + "/"));
            processBuilder.command("./start.sh");
        }
        process = processBuilder.start();

        if(process.isAlive()) {
            MCloudApi.getApi().getLogger().info("Server: " + serverName + " is Alive", ConsoleColor.CYAN);
        } else {
            MCloudApi.getApi().getLogger().info("Server: " + serverName + " is not Alive", ConsoleColor.CYAN);
        }
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

    public int getMaxMemory() {
        return maxMemory;
    }

    public int getMinMemory() {
        return minMemory;
    }

    public PaperVersions getPaperVersion() {
        return paperVersion;
    }

    public String getServerName() {
        return serverName;
    }
}
