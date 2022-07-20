package net.mcloud.runner.listener.impl;

import net.mcloud.api.eventsystem.EventHandler;
import net.mcloud.api.eventsystem.Listener;
import net.mcloud.api.eventsystem.defaultevents.server.ConsoleCommandSendEvent;
import net.mcloud.runner.MCloudRunner;

public class CommandSendListener implements Listener {

    @EventHandler
    public void onCommandSend(ConsoleCommandSendEvent event) {
        MCloudRunner.getInstance().getMCloudApi().getLogger().info("Command Listener Triggered " + event.getEventName());
    }
}
