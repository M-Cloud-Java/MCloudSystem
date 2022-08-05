package net.mcloud.runner.commands;

import net.mcloud.api.MCloudAPI;
import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandResponse;
import net.mcloud.api.servicemanager.services.ServerInstance;
import net.mcloud.api.servicemanager.versions.PaperVersions;
import org.jline.utils.AttributedString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateServerServiceCommand extends Command {
    @Override
    public CommandResponse execute(String command_name, ArrayList<String> args) {
        ServerInstance server = MCloudAPI.getApi().getServiceManager().createServerService("test", PaperVersions.PAPER_1_18_2, 4096, 4096);
        return CommandResponse.SUCCESS;
    }

    @Override
    public String usage() {
        return "This Command is useful :)";
    }

    @Override
    public String name() {
        return "create-server-service";
    }

    @Override
    public ArrayList<String> args(String[] args) {
        return new ArrayList<>();
    }
}
