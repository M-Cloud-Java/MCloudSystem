package net.mcloud.api.utils;

import net.mcloud.api.MCloudAPI;
import net.mcloud.api.eventsystem.*;
import net.mcloud.api.utils.exceptions.CloudException;

import java.lang.reflect.Method;
import java.util.*;

public class CloudManager {

    private final MCloudAPI api;

    public CloudManager(MCloudAPI api) {
        this.api = api;
    }

    public void callEvent(Event event) {
        try {
            for (RegisteredListener registeredListener : getEventListeners(event.getClass()).getRegisteredListeners()) {
                if (!registeredListener.getMCloud().isEnabled()) {
                    continue;
                }

                try {
                    registeredListener.callEvent(event);
                } catch (Exception e) {
                    api.getLogger().warn("eventError " + event.getEventName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void registerEvents(Listener listener, MCloudAPI api) {
        if (!api.isEnabled()) {
            throw new CloudException("The Cloud is not able to register this Listener " + listener.getClass().getName() + " while Cloud is null");
        }

        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            Collections.addAll(methods, publicMethods);
            Collections.addAll(methods, privateMethods);
        } catch (NoClassDefFoundError e) {
            MCloudAPI.getApi().getLogger().error("The MCloud is failed to register this Event");
            return;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> checkClass;

            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                MCloudAPI.getApi().getLogger().error("attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }

            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);

            for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    if (api.getCloudConfig().deprecatedEvents()) {
                        MCloudAPI.getApi().getLogger().warn("MCloud.cloud.deprecatedEvent " + clazz.getName());
                    }
                    break;
                }
            }
            this.registerEvent(eventClass, listener, eh.priority(), new MethodEventExecutor(method), api, eh.ignoreCancelled());
        }
    }

    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, MCloudAPI api) throws CloudException {
        this.registerEvent(event, listener, eventPriority, eventExecutor, api, false);
    }

    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, MCloudAPI api, boolean ignoreCancelled) throws CloudException {
        if (!api.isEnabled()) {
            throw new CloudException("The MCloud attempted to register " + event + " while MCloud not Online");
        }

        try {
            this.getEventListeners(event).register(new RegisteredListener(listener, eventExecutor, eventPriority, api, ignoreCancelled));
        } catch (IllegalAccessException e) {
            MCloudAPI.getApi().getLogger().error(e.getMessage());
        }
    }

    private HandlerList getEventListeners(Class<? extends Event> type) throws IllegalAccessException {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlers");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (NullPointerException e) {
            throw new IllegalAccessException("getHandlers method in " + type.getName() + " was not static!");
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) throws IllegalAccessException {
        try {
            clazz.getDeclaredMethod("getHandlers");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalAccessException("Unable to find handler list for event " + clazz.getName());
            }
        }
    }
}
