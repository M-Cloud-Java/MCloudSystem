package net.mcloud.runner.listener.impl;

import net.mcloud.api.eventsystem.EventHandler;
import net.mcloud.api.eventsystem.Listener;
import net.mcloud.api.eventsystem.defaultevents.server.MCloudStopEvent;
import net.mcloud.runner.MCloudRunner;

public class CloudStopListener implements Listener {

    @EventHandler
    public void onCloudClose(MCloudStopEvent event) {
        MCloudRunner.getInstance().getMCloudApi().getLogger().info("CloudStop Listener Triggered " + event.getEventName());
    }
}
