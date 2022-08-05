package net.mcloud.api;

import net.mcloud.api.commandsystem.CommandMap;
import net.mcloud.api.commandsystem.reader.ConsoleCommandReader;
import net.mcloud.api.modulesystem.ModuleManager;
import net.mcloud.api.servicemanager.ServiceManager;
import net.mcloud.api.utils.CloudManager;
import net.mcloud.api.utils.json.JsonHandler;
import net.mcloud.api.utils.json.MainCloudConfig;
import net.mcloud.api.utils.logger.ConsoleColor;
import net.mcloud.api.utils.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MCloudApi {
    private static MCloudApi instance;

    private final CloudManager cloudManager;
    private final CommandMap cloudCommandMap;
    private final Logger logger;
    private boolean isEnabled;
    private final ConsoleCommandReader cloudCommandHandler;
    private final ModuleManager cloudModuleManager;

    private final ServiceManager serviceManager;
    private JsonHandler jsonHandler;
    private MainCloudConfig mainCloudConfig;

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

        logger.info("Loading Settings");

        logger.info("Try to load Config Files...");
        jsonHandler = new JsonHandler("storage");
        File dir = new File("storage");
        if(!dir.exists())
            dir.mkdirs();
        File file = new File("storage/mainConfig.json");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mainCloudConfig = new MainCloudConfig("54777", "54555", "true");
            try {
                jsonHandler.parseObject(mainCloudConfig, "mainConfig.json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                mainCloudConfig = (MainCloudConfig) jsonHandler.getObject("mainConfig.json", MainCloudConfig.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        logger.info("Try to start ServiceManager...");
        serviceManager = new ServiceManager("services.json", jsonHandler);
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

    public static MCloudApi getApi() {
        return instance;
    }

    public MainCloudConfig getMainCloudConfig() {
        return mainCloudConfig;
    }

    public JsonHandler getJsonHandler() {
        return jsonHandler;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }
}
