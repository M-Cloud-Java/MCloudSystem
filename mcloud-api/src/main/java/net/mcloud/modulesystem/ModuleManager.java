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

import java.util.ArrayList;

public class ModuleManager {

    ArrayList<MCloudSubModule> modules = new ArrayList<>();

    public void registerModule(MCloudSubModule module) {
        modules.add(module);
    }

    public ArrayList<MCloudSubModule> getModules() {
        return modules;
    }
}
