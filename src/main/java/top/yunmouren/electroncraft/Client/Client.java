package top.yunmouren.electroncraft.Client;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.yunmouren.electroncraft.Browser.Browser;
import top.yunmouren.electroncraft.Browser.Handler.Handler;
import top.yunmouren.electroncraft.Browser.Handler.entry.BrowserInit;
import top.yunmouren.electroncraft.Browser.Handler.entry.EnterFullScreen;
import top.yunmouren.electroncraft.Browser.Handler.entry.LeaveFullScreen;
import top.yunmouren.electroncraft.Browser.Handler.entry.LoadFile;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@OnlyIn(Dist.CLIENT)
public class Client {
    public static ScheduledExecutorService tick = Executors.newScheduledThreadPool(1);
    public static Browser browser = new Browser();
    public Client(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        RegisterHandler();
    }
    public void RegisterHandler() {
        Handler.register("LoadFile", new LoadFile());
        Handler.register("EnterFullScreen", new EnterFullScreen());
        Handler.register("LeaveFullScreen", new LeaveFullScreen());
        Handler.register("BrowserInit", new BrowserInit());
    }
    public void onClientSetup(final FMLClientSetupEvent event) {
//        browser.api.NodeJs.start("localhost", browser.BrowserPort);
        browser.api.NodeJs.start("localhost", 9090);

        //WaitForTheBrowserToLoad
        tick.scheduleAtFixedRate(() -> {
            browser.api.tick();
        }, 1, 1, TimeUnit.SECONDS);
    }
}
