package net.mcloud.eventsystem;

import net.mcloud.utils.exceptions.EventException;

public abstract class Event {

    protected String eventName = null;
    private boolean isCancelled = false;

    public String getEventName() {
        return eventName == null ? getClass().getName() : eventName;
    }

    public boolean isCancelled() {
        if(!(this instanceof Cancellable)) {
            throw new EventException("Event is not Cancellable");
        }
        return isCancelled;
    }

    public void setCancelled(boolean value) {
        if(!(this instanceof Cancellable)) {
            throw new EventException("Event is not Cancellable");
        }
        isCancelled = value;
    }
}
