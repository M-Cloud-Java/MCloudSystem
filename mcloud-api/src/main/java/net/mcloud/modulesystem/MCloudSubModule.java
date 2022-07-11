/*
    --------------------------
    Project : MCloud
    Package : net.mcloud.api.module
    Date 23.06.2022
    @author ShortException
    --------------------------
*/


package net.mcloud.modulesystem;

import lombok.Getter;
import net.mcloud.MCloudApi;
import net.mcloud.utils.logger.Logger;

public abstract class MCloudSubModule {

    public MCloudSubModule() {
        MCloudApi.getApi().getCloudModuleManager().registerModule(this);
    }

    public abstract String getModuleName();
    public abstract void onLoad();
    public abstract void onStart();
    public abstract void onStop();

    @Getter
    public Logger logger = new Logger();

}
