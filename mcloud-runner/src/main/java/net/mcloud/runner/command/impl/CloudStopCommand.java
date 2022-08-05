package net.mcloud.runner.command.impl;

import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandResponse;

import java.util.ArrayList;

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
    public ArrayList<String> args(String[] splitCommand) {
        return new ArrayList<>();
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
