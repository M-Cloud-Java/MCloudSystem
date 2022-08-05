/*
    --------------------------
    Project : MCloudSystem
    Package : net.mcloud.api.servicemanager.services
    Date 05.08.2022
    @author ShortException
    --------------------------
*/


package net.mcloud.api.servicemanager.services;

import lombok.Getter;
import lombok.Setter;
import net.mcloud.api.servicemanager.versions.PaperVersions;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.Locale;

@Getter
public abstract class Instance {

    private final String serverName;

    private final PaperVersions paperVersion;
    @Setter
    private int minMemory = 1024;
    @Setter
    private int maxMemory = 1024;

    public Instance(String serverName, PaperVersions paperVersion) {
        this.serverName = serverName;
        this.paperVersion = paperVersion;
    }

    public abstract boolean startServer() throws IOException;

    public abstract File getPluginFolder();

    public abstract File getServerFolder();

    public abstract File getStartServerFile();

    public abstract boolean createServer();

    protected OS getOS() {
        String property = System.getProperty("os.name");
        return OS.valueOf(property.toUpperCase(Locale.ROOT));
    }

    public enum OS {
        WINDOWS,
        LINUX,
        MACOS
    }

}
