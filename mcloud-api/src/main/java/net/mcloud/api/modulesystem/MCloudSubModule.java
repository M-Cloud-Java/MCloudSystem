/*
    --------------------------
    Project : MCloud
    Package : net.mcloud.api.module
    Date 23.06.2022
    @author ShortException
    --------------------------
*/


package net.mcloud.api.modulesystem;

import lombok.Getter;
import net.mcloud.api.MCloudAPI;
import net.mcloud.api.utils.logger.Logger;

public abstract class MCloudSubModule {

    public MCloudSubModule() {
        MCloudAPI.getApi().getCloudModuleManager().registerModule(this);
    }

    public abstract String getModuleName();
    public abstract void onLoad();
    public abstract void onStart();
    public abstract void onStop();

    @Getter
    public Logger logger = new Logger();

}
