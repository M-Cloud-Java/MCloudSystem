package net.mcloud.api;

import net.mcloud.api.commandsystem.CommandMap;
import net.mcloud.api.commandsystem.reader.ConsoleCommandReader;
import net.mcloud.api.modulesystem.ModuleManager;
import net.mcloud.api.servicemanager.ServiceManager;
import net.mcloud.api.utils.CloudManager;
import net.mcloud.api.utils.config.ConfigType;
import net.mcloud.api.utils.config.types.CloudConfig;
import net.mcloud.api.utils.config.XmlConfigFile;
import net.mcloud.api.utils.logger.ConsoleColor;
import net.mcloud.api.utils.logger.Logger;

import java.io.File;

public class MCloudAPI {
    private static MCloudAPI instance;

    private final CloudManager cloudManager;
    private final CommandMap cloudCommandMap;
    private final Logger logger;
    private boolean isEnabled;
    private final ConsoleCommandReader cloudCommandHandler;
    private final ModuleManager cloudModuleManager;

    private final ServiceManager serviceManager;

    private XmlConfigFile mainConfig;

    private CloudConfig cloudConfig;

    public MCloudAPI() {
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

        logger.info("Loading Settings");

        logger.info("Try to load Config Files...", ConsoleColor.YELLOW);
        File dir = new File("storage");
        if(!dir.exists())
            dir.mkdirs();
        mainConfig = new XmlConfigFile(dir.getPath(), "cloudConfig.xml");
        if(!mainConfig.exists()) {
            logger.info("Cloud Config is not founded, the Cloud Create a new Cloud Config", ConsoleColor.YELLOW);
            cloudConfig = (CloudConfig) mainConfig.createFile(new CloudConfig(54555, 54777, true));
            logger.info("The Cloud Config is successful created", ConsoleColor.YELLOW);
        }
        logger.info("Cloud Config is founded, the Cloud try to Load the Cloud Config", ConsoleColor.YELLOW);
        cloudConfig = (CloudConfig) mainConfig.loadFile(ConfigType.CLOUD_CONFIG);
        logger.info("Cloud Config is successful loaded", ConsoleColor.YELLOW);

        logger.info("Try to start ServiceManager...");
        serviceManager = new ServiceManager();
        logger.info("Finished Loading ServiceManager!");

        logger.info("Finished Loading Config Files!");

        this.cloudManager = new CloudManager(instance);
        this.cloudCommandHandler = new ConsoleCommandReader(new CommandMap());
        this.cloudCommandMap = this.cloudCommandHandler.commandMap();

        this.cloudModuleManager = new ModuleManager();
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public CloudManager getCloudManager() {
        return cloudManager;
    }

    public CommandMap getCloudCommandMap() {
        return cloudCommandMap;
    }

    public ConsoleCommandReader getCloudCommandHandler() {
        return cloudCommandHandler;
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

    public static MCloudAPI getApi() {
        return instance;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public CloudConfig getCloudConfig() {
        return cloudConfig;
    }
}
