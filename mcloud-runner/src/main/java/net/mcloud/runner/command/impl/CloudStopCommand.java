package net.mcloud.runner.command.impl;

import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandResponse;
import org.jline.utils.AttributedString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudStopCommand extends Command {

    @Override
    public String usage() {
        return "Shutdown the cloud";
    }

    @Override
    public String name() {
        return "stop";
    }

    @Override
    public Map<String, List<AttributedString>> widgetOpt() {
        return new HashMap<>();
    }

    @Override
    public List<AttributedString> desc() {
        return List.of(new AttributedString("Stops the cloud and all running services"));
    }

    @Override
    public CommandResponse execute(String command_name, ArrayList<String> args) {
        if (args.size() == 0) {
            System.exit(0);
            logger().warn("Die Cloud wird nun gestoppt!");
            return CommandResponse.SUCCESS;
        } else {
            logger().warn("For this Command it is not allowed to write Arguments behind them! please use stop");
            return CommandResponse.WARNING;
        }
    }
}
