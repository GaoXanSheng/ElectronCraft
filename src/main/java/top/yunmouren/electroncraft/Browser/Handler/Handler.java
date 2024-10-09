package top.yunmouren.electroncraft.Browser.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import top.yunmouren.electroncraft.Browser.Handler.inherit.IHandler;
import top.yunmouren.electroncraft.ElectronCraft;


import java.util.HashMap;

public class Handler {
    private static final  HashMap<String,Class<? extends IHandler>> handlerMap = new HashMap<>();
    public Handler(String receiveMessage) {
        JsonObject jsonObject = new Gson().fromJson(receiveMessage, JsonObject.class);
        // GetTheFieldsInTheJSONObject
        String type = jsonObject.get("Type").getAsString();
        if (handlerMap.containsKey(type)) {
            try {
                Class<?> IHandlerClass = handlerMap.get(type);
                IHandler handler = (IHandler) IHandlerClass.getDeclaredConstructor().newInstance();
                handler.Handler(jsonObject);
            } catch (Exception e) {
                ElectronCraft.logger.error(e.getMessage());
            }
        }
    }

    public static <T extends IHandler> void register(String type, Class<T> handler) {
        handlerMap.put(type, handler);
    }
}
