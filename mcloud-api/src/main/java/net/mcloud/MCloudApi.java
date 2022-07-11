package net.mcloud;

import net.mcloud.commandsystem.CommandMap;
import net.mcloud.commandsystem.ConsoleCommandHandler;
import net.mcloud.modulesystem.MCloudSubModule;
import net.mcloud.modulesystem.ModuleManager;
import net.mcloud.utils.CloudManager;
import net.mcloud.utils.json.JsonConfigBuilder;
import net.mcloud.utils.logger.ConsoleColor;
import net.mcloud.utils.logger.Logger;

import javax.swing.*;

public class MCloudApi {
    private static MCloudApi instance;

    private final CloudManager cloudManager;
    private final CommandMap cloudCommandMap;
    private final Logger logger;
    private final JsonConfigBuilder cloudConfigManager;
    private boolean isEnabled;
    private ConsoleCommandHandler cloudCommandHandler;
    private ModuleManager cloudModuleManager;

    public MCloudApi() {
        instance = this;
        this.logger = new Logger();

        logger.info("""          
                                
                ___  ___         _____  _                    _\s
                |  \\/  |        /  __ \\| |                  | |
                | .  . | ______ | /  \\/| |  ___   _   _   __| |
                | |\\/| ||______|| |    | | / _ \\ | | | | / _` |
                | |  | |        | \\__/\\| || (_) || |_| || (_| |
                \\_|  |_/         \\____/|_| \\___/  \\__,_| \\__,_|
                                                              \s
                """, ConsoleColor.CYAN);

        logger.info("Cloud starting... ", ConsoleColor.GREEN);
        isEnabled = true;

        this.cloudConfigManager = new JsonConfigBuilder("cloudsettings", "settings");

        logger.info("Loading Settings");
        setDefaultSettings();

        this.cloudManager = new CloudManager(instance);
        this.cloudCommandHandler = new ConsoleCommandHandler(new CommandMap());
        this.cloudCommandMap = this.cloudCommandHandler.getCommandMap();

        this.cloudModuleManager = new ModuleManager();
    }

    private void setDefaultSettings() {
        cloudConfigManager.setInteger("udp-port", 54777, 54777);
        cloudConfigManager.setInteger("tcp-port", 54555, 54555);
        cloudConfigManager.setBoolean("deprecated-events", true, true);
        cloudConfigManager.setInteger("max-cloud-proxy-count", 1, 1);
    }

    public CloudManager getCloudManager() {
        return cloudManager;
    }

    public CommandMap getCloudCommandMap() {
        return cloudCommandMap;
    }

    public ConsoleCommandHandler getCloudCommandHandler() {
        return cloudCommandHandler;
    }

    public JsonConfigBuilder getCloudConfigManager() {
        return cloudConfigManager;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Logger getLogger() {
        return logger;
    }

    public ModuleManager getCloudModuleManager() {
        return cloudModuleManager;
    }

    public static MCloudApi getApi() {
        return instance;
    }
}
