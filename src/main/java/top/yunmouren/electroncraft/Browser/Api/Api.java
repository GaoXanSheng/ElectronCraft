package top.yunmouren.electroncraft.Browser.Api;


import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import top.yunmouren.electroncraft.Browser.IPC.TcpClient;

import java.util.Timer;
import java.util.TimerTask;

public class Api {
    public TcpClient NodeJs = new TcpClient();

    public Api() {

    }

    public void CombiningURL(String type, JsonObject data) {
        JsonObject json = new JsonObject();
        json.addProperty("ComeFrom", "Minecraft");
        json.addProperty("Reach", "Node");
        json.addProperty("Type", type);
        json.add("Data", data);
        NodeJs.sendMessage(json);
    }

    public void CombiningURL(String type, String data) {
        JsonObject json = new JsonObject();
        json.addProperty("ComeFrom", "Minecraft");
        json.addProperty("Reach", "Node");
        json.addProperty("Type", type);
        json.addProperty("Data", data);
        NodeJs.sendMessage(json);
    }

    public void loadUrl(String url) {
        CombiningURL("loadUrl", url);
    }

    public void setBrowserSize(int width , int height) {
        JsonObject data = new JsonObject();
        data.addProperty("width", width);
        data.addProperty("height", height);
        CombiningURL("setBrowserSize", data);
    }
    /**
     * Set how many frames per second the browser transmits
     */
    public void setFrameRate(int s) {
        CombiningURL("setFrameRate", String.valueOf(s));
    }
    public void openDevTools() {
        CombiningURL("openDevTools", "");
    }
}
