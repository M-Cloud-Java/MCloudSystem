package net.mcloud.api.eventsystem;

public interface Cancellable {

    boolean isCancelled();
    void setCancelled(boolean value);
}
