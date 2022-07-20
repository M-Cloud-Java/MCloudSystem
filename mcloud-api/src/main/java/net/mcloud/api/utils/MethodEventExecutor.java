package net.mcloud.api.utils;

import net.mcloud.api.eventsystem.Event;
import net.mcloud.api.eventsystem.EventExecutor;
import net.mcloud.api.eventsystem.Listener;
import net.mcloud.api.utils.exceptions.EventException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodEventExecutor implements EventExecutor {

    private final Method method;

    public MethodEventExecutor(Method method) {
        this.method = method;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            Class<Event>[] params = (Class<Event>[]) method.getParameterTypes();
            for (Class<Event> param : params) {
                if (param.isAssignableFrom(event.getClass())) {
                    method.invoke(listener, event);
                    break;
                }
            }
        } catch (InvocationTargetException ex) {
            throw new EventException(ex.getCause());
        } catch (ClassCastException ex) {

        } catch (Throwable t) {
            throw new EventException(t);
        }
    }

    public Method getMethod() {
        return method;
    }
}
