package net.mcloud.api.eventsystem;

import net.mcloud.api.MCloudAPI;
import net.mcloud.api.utils.exceptions.EventException;

public class RegisteredListener {

    private final Listener listener;

    private final EventPriority eventPriority;

    private final MCloudAPI api;

    private final EventExecutor eventExecutor;

    private  final boolean ignoreCancelled;

    public RegisteredListener(Listener listener, EventExecutor eventExecutor, EventPriority eventPriority, MCloudAPI api, boolean ignoreCancelled) {
        this.listener = listener;
        this.eventPriority = eventPriority;
        this.eventExecutor = eventExecutor;
        this.ignoreCancelled = ignoreCancelled;
        this.api = api;
    }

    public void callEvent(Event event) throws EventException {
        if(event instanceof Cancellable) {
            if( event.isCancelled() && isIgnoreCancelled()) {
                return;
            }
        }
        eventExecutor.execute(listener, event);
    }

    public Listener getListener() {
        return listener;
    }

    public MCloudAPI getMCloud() {
        return api;
    }

    public EventPriority getEventPriority() {
        return eventPriority;
    }

    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }
}
