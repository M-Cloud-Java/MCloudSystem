package net.mcloud.runner.command.impl;

import net.mcloud.api.MCloudApi;
import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandResponse;
import net.mcloud.api.servicemanager.services.ServerInstance;
import net.mcloud.api.servicemanager.versions.PaperVersions;
import net.mcloud.runner.MCloudRunner;
import org.jline.utils.AttributedString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateServerServiceCommand extends Command {
    @Override
    public CommandResponse execute(String command_name, ArrayList<String> args) {
        ServerInstance server = MCloudApi.getApi().getServiceManager().createServerService("test", PaperVersions.PAPER_1_18_2, 4096, 4096);
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
    public Map<String, List<AttributedString>> widgetOpt() {
        return new HashMap<>();
    }

    @Override
    public List<AttributedString> desc() {
        return List.of(new AttributedString("Creates a Server Service LUL"));
    }
}
