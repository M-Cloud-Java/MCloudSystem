package net.mcloud.runner.command.impl;

import net.mcloud.api.MCloudApi;
import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandResponse;
import net.mcloud.api.utils.logger.ConsoleColor;
import org.jline.utils.AttributedString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartServiceCommand extends Command {
    @Override
    public CommandResponse execute(String command_name, ArrayList<String> args) {
        if(args.size() == 2) {
            String serviceType = args.get(0);
            String serviceName = args.get(1);
            if(serviceType.equals("server")) {
                    MCloudApi.getApi().getServiceManager().getServerInstances().forEach(serverInstance -> {
                        if(serverInstance.getServerName().equals(serviceName)) {
                            try {
                                serverInstance.startServer();
                                MCloudApi.getApi().getLogger().info("The Cloud start the Server: " + serverInstance.getServerName() + " with the Version: " + serverInstance.getPaperVersion().name(), ConsoleColor.CYAN);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            MCloudApi.getApi().getLogger().info("The specified serviceName: " + serviceName + " is not found!", ConsoleColor.YELLOW);
                        }
                    });
            } else {
                MCloudApi.getApi().getLogger().info("The specified serviceType: " + serviceType + " is unknown!", ConsoleColor.RED);
                return CommandResponse.WARNING;
            }
        } else {
            return CommandResponse.ERROR;
        }
        return CommandResponse.NONE;
    }

    @Override
    public String usage() {
        return "With this Command you can start a Service";
    }

    @Override
    public String name() {
        return "start-service";
    }

    @Override
    public Map<String, List<AttributedString>> widgetOpt() {
        return new HashMap<>();
    }

    @Override
    public List<AttributedString> desc() {
        return new ArrayList<>();
    }
}
