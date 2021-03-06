package net.infstudio.foodcraftreloaded.utils.loader;

import net.infstudio.foodcraftreloaded.FoodCraftReloaded;
import net.infstudio.foodcraftreloaded.utils.loader.annotation.Load;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class LoaderManager {
    private final Map<Class<?>, Object> loaderInstanceMap = new HashMap<>();
    private final Map<LoaderState, Collection<Method>> stateLoaderMap = new HashMap<>();

    public void addLoader(Class<?> loaderClass) {
        try {
            for (Method method : loaderClass.getMethods())
                for (Annotation annotation : method.getDeclaredAnnotations())
                    if (annotation.annotationType().equals(Load.class))
                        if (method.getParameterCount() == 0 || (FMLStateEvent.class.isAssignableFrom(method.getParameterTypes()[0]) && method.getParameterTypes()[0].equals(((Load) annotation).value().getEvent().getClass()))) {
                            Collection<Method> methods = stateLoaderMap.getOrDefault(((Load) annotation).value(), new ArrayList<>());
                            if (!methods.contains(method))
                                methods.add(method);
                            stateLoaderMap.put(((Load) annotation).value(), methods);
                        }
            loaderInstanceMap.put(loaderClass, loaderClass.newInstance());
        } catch (Exception e) {
            FoodCraftReloaded.getLogger().warn("Un-able to register loader " + loaderClass.getName(), e);
        }
    }

    public <T extends FMLStateEvent> void invoke (T event, LoaderState state, Side side) {
        stateLoaderMap.values().forEach(methods -> methods.forEach(method -> {
            if (method.getAnnotation(Load.class).side().equals(side))
                if (method.getParameterCount() == 0 && method.getAnnotation(Load.class).value().equals(state))
                    try {
                        method.invoke(loaderInstanceMap.get(method.getDeclaringClass()));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        FoodCraftReloaded.getLogger().warn("Un-able to invoke method " + method.getName(), e);
                    }
                else if (method.getParameterCount() == 1 && method.getParameterTypes()[1].equals(event.getClass()))
                    try {
                        method.invoke(loaderInstanceMap.get(method.getDeclaringClass()), event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        FoodCraftReloaded.getLogger().warn("Un-able to invoke method " + method.getName(), e);
                    }
        }));
    }

    public Map<Class<?>, Object> getLoaderInstanceMap() {
        return loaderInstanceMap;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getLoader(Class<T> loaderClass) {
        try {
            return Optional.of((T) loaderInstanceMap.get(loaderClass));
        } catch (ClassCastException ignored) {
            return Optional.empty();
        }
    }

    public Map<LoaderState, Collection<Method>> getStateLoaderMap() {
        return stateLoaderMap;
    }

}
