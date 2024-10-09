package top.yunmouren.electroncraft.Client.Network;

import icyllis.modernui.ModernUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.yunmouren.electroncraft.Client.Client;
import top.yunmouren.electroncraft.Client.GUIScreen;


@OnlyIn(Dist.CLIENT)
public class ClientEnumeration {
    public ClientEnumeration(String ctx, String body) {
        switch (ctx) {
            case "OpenDevTools":
                Client.browser.api.openDevTools();
                break;
            case "loadUrl":
                Client.browser.api.loadUrl(body);
                break;
            case "openGUI":
                openGUIOnClient();break;
        }
    }
    private static void openGUIOnClient() {
        Minecraft.getInstance().setScreen(new GUIScreen("TEST"));
    }

    public static void closeGUIOnClient() {
        Minecraft.getInstance().setScreen(null);
    }
}
