package net.mcloud.api.servicemanager.services;

import net.mcloud.api.MCloudApi;
import net.mcloud.api.servicemanager.versions.PaperVersions;
import net.mcloud.api.utils.Downloader;
import net.mcloud.api.utils.logger.ConsoleColor;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class ServerInstance {

    private File serverFolder;
    private File pluginFolder;
    private File eulaFile;
    private File startServerFile;

    public ServerInstance(String serverName, PaperVersions serverVersion) {
        serverFolder = new File("services/" + serverName);
        if(!serverFolder.exists())
            serverFolder.mkdirs();
        else
            MCloudApi.getApi().getLogger().info("Please choose a other Name for this Server because the Service is already defined!", ConsoleColor.RED);

        pluginFolder = new File("services/" + serverName + "/plugins");
        if(!pluginFolder.exists())
            pluginFolder.mkdirs();

        eulaFile = new File("services/" + serverName + "/eula.txt");
        try {
            eulaFile.createNewFile();
            FileWriter writer = new FileWriter(eulaFile);
            writer.write(
                    "#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://aka.ms/MinecraftEULA).\n" +
                    "eula=true");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String osName = System.getProperty("os.name");
        if(osName.equals("Linux")) {
            startServerFile = new File("services/" + serverName + "/start.sh");
            try {
                startServerFile.createNewFile();
                FileWriter writer = new FileWriter(startServerFile);
                writer.write("java -server -jar paper-server.jar");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(osName.contains("Windows")) {
            startServerFile = new File("services/" + serverName + "/start.bat");
            try {
                startServerFile.createNewFile();
                FileWriter writer = new FileWriter(startServerFile);
                writer.write("java -server -jar paper-server.jar");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MCloudApi.getApi().getLogger().error("The Cloud can not start the Server because you use a wrong Os System");
        }

            try {
                new Downloader(new URL(serverVersion.getDownloadUrl()), "services/" + serverName + "/paper-server.jar").downloadFile();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        try {
            startServer(serverName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer(String serverName) throws IOException {
       File executableJar = new File("services/" + serverName + "/", "paper-server.jar");
       ProcessBuilder processBuilder = new ProcessBuilder("java -Dcom.mojang.eula.agree=true -jar " + executableJar.getPath());
       Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
       while (process.isAlive()) {
           MCloudApi.getApi().getLogger().info(reader.readLine());
       }
    }
}
