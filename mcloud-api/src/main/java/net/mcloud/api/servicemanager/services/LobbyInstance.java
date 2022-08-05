package net.mcloud.api.servicemanager.services;

import net.mcloud.api.servicemanager.versions.PaperVersions;

import java.io.File;
import java.io.IOException;

public class LobbyInstance extends Instance{
    public LobbyInstance(String serverName, PaperVersions paperVersion) {
        super(serverName, paperVersion);
    }

    @Override
    public boolean startServer() throws IOException {
        return false;
    }

    @Override
    public File getPluginFolder() {
        return null;
    }

    @Override
    public File getServerFolder() {
        return null;
    }

    @Override
    public File getStartServerFile() {
        return null;
    }

    @Override
    public boolean createServer() {
        return false;
    }
}
