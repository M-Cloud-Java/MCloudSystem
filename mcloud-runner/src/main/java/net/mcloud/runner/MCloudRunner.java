package net.mcloud.runner;

import net.mcloud.api.MCloudApi;
import net.mcloud.api.eventsystem.HandlerList;
import net.mcloud.api.eventsystem.defaultevents.server.MCloudStopEvent;
import net.mcloud.api.modulesystem.MCloudSubModule;
import net.mcloud.api.servicemanager.ServiceManager;
import net.mcloud.api.utils.logger.ConsoleColor;
import net.mcloud.runner.command.impl.CloudStopCommand;
import net.mcloud.runner.command.impl.CreateServerServiceCommand;
import net.mcloud.runner.command.impl.HelpCommand;
import net.mcloud.runner.listener.impl.CloudStopListener;
import net.mcloud.runner.listener.impl.CommandSendListener;

public class MCloudRunner {
    private static MCloudRunner instance;
    private final MCloudApi mCloudApi;
    private final ServiceManager serviceManager;
    public MCloudRunner() {
        instance = this;
        Runtime.getRuntime().addShutdownHook(new ShutdownTask());
        mCloudApi = new MCloudApi();



        mCloudApi.getLogger().info("Loading Modules");
        mCloudApi.getCloudModuleManager().getModules().forEach(MCloudSubModule::onLoad);
        mCloudApi.getLogger().info("Starting Modules");
        mCloudApi.getCloudModuleManager().getModules().forEach(MCloudSubModule::onStart);

        mCloudApi.getLogger().info("Try to register Default Commands...");
        registerCommands();
        mCloudApi.getLogger().info("Finished Loading Default Commands!");

        mCloudApi.getLogger().info("Try to register Default Listener...");
        registerListener();
        mCloudApi.getLogger().info("Finished Loading Default Listener!");

        mCloudApi.getLogger().info("Try to start ServiceManager...");
        serviceManager = new ServiceManager();
        mCloudApi.getLogger().info("Finished Loading ServiceManager!");

        mCloudApi.getLogger().info("Starting ConsoleInput");
        mCloudApi.getCloudCommandHandler().startConsoleInput();
    }

    private void registerCommands() {
        mCloudApi.getCloudCommandMap().register(new CloudStopCommand());
        mCloudApi.getCloudCommandMap().register(new HelpCommand());
        mCloudApi.getCloudCommandMap().register(new CreateServerServiceCommand());
    }

    private void registerListener() {
        mCloudApi.getCloudManager().registerEvents(new CloudStopListener(), mCloudApi);
        mCloudApi.getCloudManager().registerEvents(new CommandSendListener(), mCloudApi);
    }

    public MCloudApi getMCloudApi() {
        return mCloudApi;
    }

    public static MCloudRunner getInstance() {
        return instance;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public static void main(String[] args) {
        new MCloudRunner();
    }

    private class ShutdownTask extends Thread {
        @Override
        public void run() {
            mCloudApi.getLogger().warn("The cloud is trying to shutdown");
            MCloudStopEvent event = new MCloudStopEvent("The System Shutdown Normal");
            mCloudApi.getCloudManager().callEvent(event);
            mCloudApi.getLogger().info("Stopping Modules ...");
            mCloudApi.getCloudModuleManager().getModules().forEach(MCloudSubModule::onStop);
            mCloudApi.getLogger().warn("Stopped Modules!");
            mCloudApi.setEnabled(false);
            HandlerList.unregisterAll();
            mCloudApi.getLogger().info("Good bye!", ConsoleColor.PURPLE);
        }
    }
}
