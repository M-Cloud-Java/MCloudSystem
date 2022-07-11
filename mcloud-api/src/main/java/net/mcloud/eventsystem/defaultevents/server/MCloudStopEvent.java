package net.mcloud.eventsystem.defaultevents.server;

import net.mcloud.eventsystem.Event;
import net.mcloud.eventsystem.HandlerList;

public class MCloudStopEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String reason;

    public MCloudStopEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
