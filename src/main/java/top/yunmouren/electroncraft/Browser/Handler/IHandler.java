package top.yunmouren.electroncraft.Browser.Handler;

import com.google.gson.JsonObject;

/**
 * If you want to handle messages from the web interface, you need to inherit this method
 */
public abstract class IHandler {
    public abstract void Handler(JsonObject receiveMessage);
}
