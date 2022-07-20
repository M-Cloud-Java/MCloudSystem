package net.mcloud.api.eventsystem;

import net.mcloud.api.utils.exceptions.EventException;

public interface EventExecutor {

    void execute(Listener listener, Event event) throws EventException;
}
