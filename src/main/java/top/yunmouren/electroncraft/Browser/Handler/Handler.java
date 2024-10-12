package top.yunmouren.electroncraft.Browser.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import top.yunmouren.electroncraft.ElectronCraft;


import java.util.HashMap;

public class Handler {
    private static final HashMap<String, Object> handlerMap = new HashMap<>();

    public Handler(String receiveMessage) {
        JsonObject jsonObject = new Gson().fromJson(receiveMessage, JsonObject.class);
        String type = jsonObject.get("Type").getAsString();
        if (handlerMap.containsKey(type)) {
            try {
                IHandler handler = (IHandler) handlerMap.get(type);
                handler.Handler(jsonObject);
            } catch (Exception e) {
                ElectronCraft.logger.error(e.getMessage());
            }
        }
    }

    public static <T extends IHandler> void register(String type, T handler) {
        handlerMap.put(type, handler);
    }
}
