package top.yunmouren.electroncraft.Browser.Handler.inherit;

import com.google.gson.JsonObject;

/**
 * If you want to handle messages from the web interface, you need to inherit this method
 * <p>JsCode...
 * window.electron.ipcRenderer.invoke('Handler-processorAllocation',{ <br>
 * 	to:'Minecraft',  <br/>
 * 	from:'Browser' <br/>
 * 	type: string   //This type is an instance that triggers the class top.yunmouren.electron.Browser.Handler.register(type)   <br/>
 * 	data: any<br/>
 * }) <br/>
 */
public abstract class IHandler {
    public abstract void Handler(JsonObject receiveMessage);
}
