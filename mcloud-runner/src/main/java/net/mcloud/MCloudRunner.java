package net.mcloud;

import net.mcloud.modulesystem.MCloudSubModule;

public class MCloudRunner {
    private static MCloudRunner instance;
    private MCloudApi mCloudApi;
    public MCloudRunner() {
        instance = this;
        mCloudApi = new MCloudApi();

        mCloudApi.getLogger().info("Loading Modules");
        mCloudApi.getCloudModuleManager().getModules().forEach(MCloudSubModule::onLoad);
        mCloudApi.getLogger().info("Starting Modules");
        mCloudApi.getCloudModuleManager().getModules().forEach(MCloudSubModule::onStart);

        mCloudApi.getLogger().info("Starting ConsoleInput");
        mCloudApi.getCloudCommandHandler().startConsoleInput();
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
}
