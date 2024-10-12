package top.yunmouren.electroncraft.Browser.Handler.entry;

import com.google.gson.JsonObject;
import top.yunmouren.electroncraft.Browser.Handler.IHandler;
import top.yunmouren.electroncraft.Client.Client;

public class BrowserInit extends IHandler {
    @Override
    public void Handler(JsonObject receiveMessage) {
        Client.tick.shutdown();
        Client.browser.windowsApi.overlapWindows();
    }
}
