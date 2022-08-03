package net.mcloud.runner;

import net.mcloud.api.MCloudApi;
import net.mcloud.api.eventsystem.HandlerList;
import net.mcloud.api.eventsystem.defaultevents.server.MCloudStopEvent;
import net.mcloud.api.modulesystem.MCloudSubModule;
import net.mcloud.api.utils.logger.ConsoleColor;
import net.mcloud.runner.command.impl.CloudStopCommand;
import net.mcloud.runner.command.impl.CreateServerServiceCommand;
import net.mcloud.runner.command.impl.HelpCommand;
import net.mcloud.runner.command.impl.StartServiceCommand;
import net.mcloud.runner.listener.impl.CloudStopListener;
import net.mcloud.runner.listener.impl.CommandSendListener;

public class MCloudRunner {
    private static MCloudRunner instance;
    private final MCloudApi mCloudApi;
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

        mCloudApi.getLogger().info("Starting ConsoleInput");
        mCloudApi.getCloudCommandHandler().startConsoleInput();
    }

    private void registerCommands() {
        mCloudApi.getCloudCommandMap().register(new CloudStopCommand());
        mCloudApi.getCloudCommandMap().register(new HelpCommand());
        mCloudApi.getCloudCommandMap().register(new CreateServerServiceCommand());
        mCloudApi.getCloudCommandMap().register(new StartServiceCommand());
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


    public static void main(String[] args) {
        new MCloudRunner();
    }

    private class ShutdownTask extends Thread {
        @Override
        public void run() {
            mCloudApi.getLogger().warn("The cloud is trying to shutdown");
            MCloudStopEvent event = new MCloudStopEvent("The System Shutdown Normal");
            mCloudApi.getCloudManager().callEvent(event);
            mCloudApi.getLogger().info("Save Service Config ...");
            mCloudApi.getServiceManager().saveServices("services.json", mCloudApi.getJsonHandler());
            mCloudApi.getLogger().info("Stopping Modules ...");
            mCloudApi.getCloudModuleManager().getModules().forEach(MCloudSubModule::onStop);
            mCloudApi.getLogger().warn("Stopped Modules!");
            mCloudApi.setEnabled(false);
            HandlerList.unregisterAll();
            mCloudApi.getLogger().info("Good bye!", ConsoleColor.PURPLE);
        }
    }

}
