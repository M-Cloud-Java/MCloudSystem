package net.mcloud.commandsystem;

import net.mcloud.MCloudApi;
import net.mcloud.utils.logger.Logger;
import org.jline.utils.AttributedString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Command {

    public Logger logger() {
        return MCloudApi.getApi().getLogger();
    }

    public abstract CommandResponse execute(String command_name, ArrayList<String> args);

    public abstract String usage();

    public abstract String name();

    public abstract Map<String, List<AttributedString>> widgetOpt();

    public abstract List<AttributedString> desc();
}
