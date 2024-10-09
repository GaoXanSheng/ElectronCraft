package top.yunmouren.electroncraft.Client;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.yunmouren.electroncraft.Browser.Browser;
import top.yunmouren.electroncraft.Browser.Handler.Handler;
import top.yunmouren.electroncraft.Browser.Handler.entry.EnterFullScreen;
import top.yunmouren.electroncraft.Browser.Handler.entry.LeaveFullScreen;
import top.yunmouren.electroncraft.Browser.Handler.entry.LoadFile;
import top.yunmouren.electroncraft.Browser.Handler.entry.Render;


@OnlyIn(Dist.CLIENT)
public class Client {

    public static Browser browser = new Browser();
    public Client(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        RegisterHandler();
    }
    public void RegisterHandler() {
        Handler.register("LoadFile", LoadFile.class);
        Handler.register("EnterFullScreen", EnterFullScreen.class);
        Handler.register("LeaveFullScreen", LeaveFullScreen.class);
        Handler.register("Render", Render.class);
    }
    public void onClientSetup(final FMLClientSetupEvent event) {
        browser.api.NodeJs.start("localhost", 9090);
    }
}
