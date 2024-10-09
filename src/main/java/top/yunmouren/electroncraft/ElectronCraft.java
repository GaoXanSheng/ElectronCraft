package top.yunmouren.electroncraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import top.yunmouren.electroncraft.Client.Client;
import top.yunmouren.electroncraft.Server.Server;
import top.yunmouren.electroncraft.Tools.Log;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("electroncraft")
public class ElectronCraft {
    public static final String MOD_ID = "electroncraft";
    public static final Log logger = new Log();
    public  ElectronCraft() {
        logger.info("ElectronCraft init");
        if (FMLLoader.getLaunchHandler().getDist() == Dist.CLIENT){
            new Client();
        }
        new Server();
    }
}
