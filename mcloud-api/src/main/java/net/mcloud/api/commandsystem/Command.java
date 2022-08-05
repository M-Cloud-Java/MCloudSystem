package net.mcloud.api.commandsystem;

import net.mcloud.api.MCloudApi;
import net.mcloud.api.utils.logger.Logger;

import java.util.ArrayList;

public abstract class Command {

    public Logger logger() {
        return MCloudApi.getApi().getLogger();
    }

    public abstract CommandResponse execute(String commandName, ArrayList<String> args);

    public abstract String usage();

    public abstract String name();

    public abstract ArrayList<String> args(String[] args);
}
