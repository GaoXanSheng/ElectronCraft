package top.yunmouren.electroncraft.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


public class GUIScreen extends Screen {

    public GUIScreen(String argument) {
        super(Component.nullToEmpty(argument));
    }

    @Override
    protected void init() {
        super.init();
    }


    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {
        super.render(poseStack, mouseX, mouseY, delta);
    }

    @Override
    public void mouseMoved(double x, double y) {
        super.mouseMoved(x, y);
    }

    @Override
    public boolean mouseClicked(double x, double y, int key) {
        return super.mouseClicked(x, y, key);
    }

    @Override
        public boolean keyPressed(int keyCode, int p_96553_, int p_96554_) {
        return super.keyPressed(keyCode, p_96553_, p_96554_);
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
    }
    @Override
    public void onClose() {
        super.onClose();
    }
}
