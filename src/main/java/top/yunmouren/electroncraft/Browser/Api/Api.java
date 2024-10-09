package top.yunmouren.electroncraft.Browser.Api;


import com.google.gson.JsonObject;
import top.yunmouren.electroncraft.Browser.IPC.TcpClient;

import java.util.Timer;
import java.util.TimerTask;

public class Api {
    public TcpClient NodeJs = new TcpClient();
    public Api(){

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
        CombiningURL("LoadUrl", url);
    }

    public void openDevTools() {
        CombiningURL("OpenDevTools", "");
    }

    private Timer debounceTimer;

    public void setPosition(Number width, Number height) {
        // 如果之前的定时任务未完成，则取消它
        if (debounceTimer != null) {
            debounceTimer.cancel();
        }
        // 创建新的定时器任务
        debounceTimer = new Timer();
        debounceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                var data = new JsonObject();
                data.addProperty("width", width);
                data.addProperty("height", height);
                CombiningURL("SetPosition", data);
            }
        }, 200);
    }
}
