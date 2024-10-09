package top.yunmouren.electroncraft.Client;

import com.cinemamod.mcef.MCEFRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;

import static top.yunmouren.electroncraft.Browser.Handler.entry.Render.ToBeRendered;
import static top.yunmouren.electroncraft.Browser.Handler.entry.Render.setIsRender;

public class GUIScreen extends Screen {
    public Minecraft minecraft = Minecraft.getInstance();

    public GUIScreen(String argument) {
        super(Component.nullToEmpty(argument));
    }

    @Override
    protected void init() {
        Client.browser.api.setBrowserSize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
        mcefRenderer.initialize();
        setIsRender(true);
        super.init();
    }

    private final MCEFRenderer mcefRenderer = new MCEFRenderer(true);

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {
        if (ToBeRendered.isEmpty()) return;
        var frame = ToBeRendered.get(0);
        mcefRenderer.onPaint(frame.imageBuffer, frame.width, frame.height);
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, mcefRenderer.getTextureID());
        Tesselator t = Tesselator.getInstance();
        BufferBuilder buffer = t.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(0, height, 0).uv(0.0f, 1.0f).color(255, 255, 255, 255).endVertex();
        buffer.vertex(width, height, 0).uv(1.0f, 1.0f).color(255, 255, 255, 255).endVertex();
        buffer.vertex(width, 0, 0).uv(1.0f, 0.0f).color(255, 255, 255, 255).endVertex();
        buffer.vertex(0, 0, 0).uv(0.0f, 0.0f).color(255, 255, 255, 255).endVertex();
        t.end();
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableDepthTest();
        //Make sure that at least one frame is available when render is called, and that the frames are synchronized
        if (ToBeRendered.size() > 1) {
            ToBeRendered.remove(0);
        }
        super.render(poseStack, mouseX, mouseY, delta);
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        Client.browser.api.setBrowserSize(width, height);
    }

    @Override
    public void onClose() {
        mcefRenderer.cleanup();
        setIsRender(false);
        super.onClose();
    }
}
