package net.mcloud.runner.command.impl;

import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandResponse;
import net.mcloud.api.servicemanager.services.ServerInstance;
import net.mcloud.api.servicemanager.versions.PaperVersions;
import net.mcloud.runner.MCloudRunner;

import java.util.ArrayList;

public class CreateServerServiceCommand extends Command {
    @Override
    public CommandResponse execute(String commandName, ArrayList<String> args) {

        if (args.size() != 1) {
            this.logger().warn("You have to set a name before you can create a new service!");
            this.logger().warn("create-server-service <name>");
            return CommandResponse.ERROR;
        }

        ServerInstance server = MCloudRunner.getInstance().getServiceManager().createServerService("test", PaperVersions.PAPER_1_18_2);
        return CommandResponse.SUCCESS;
    }

    @Override
    public String usage() {
        return "Creates a Server Service for Testing stuff";
    }

    @Override
    public String name() {
        return "create-server-service";
    }

    @Override
    public ArrayList<String> args(String[] args) {

        var list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("[name]");
        }

        return list;
    }
}
