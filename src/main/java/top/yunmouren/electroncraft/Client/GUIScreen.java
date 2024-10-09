package top.yunmouren.electroncraft.Client;

import com.cinemamod.mcef.MCEFRenderer;
import com.mojang.blaze3d.pipeline.RenderTarget;
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
    public GUIScreen(String argument) {
        super(Component.nullToEmpty(argument));
    }

    @Override
    protected void init() {
        mcefRenderer.initialize();
        setIsRender(true);
        super.init();
    }

    public static MCEFRenderer mcefRenderer = new MCEFRenderer(true);
    private static final int BROWSER_DRAW_OFFSET = 20;
    private FrameBuffer framebuffer;

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {
        if (ToBeRendered.isEmpty()) return;

        // 初始化或更新 RenderTarget 大小
        if (framebuffer == null || framebuffer.width != this.width || framebuffer.height != this.height) {
            if (framebuffer != null) framebuffer.destroyBuffers();
            framebuffer = new FrameBuffer(true);
            framebuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }

        // 绑定 RenderTarget，开始渲染到帧缓冲区
        framebuffer.bindWrite(true);
        framebuffer.clear(Minecraft.ON_OSX);

        var frame = ToBeRendered.get(0);
        mcefRenderer.onPaint(frame.imageBuffer, frame.width, frame.height);
        ToBeRendered.remove(0);

        // 在帧缓冲上渲染内容
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, mcefRenderer.getTextureID());

        Tesselator t = Tesselator.getInstance();
        BufferBuilder buffer = t.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(BROWSER_DRAW_OFFSET, height - BROWSER_DRAW_OFFSET, 0).uv(0.0f, 1.0f).color(255, 255, 255, 255).endVertex();
        buffer.vertex(width - BROWSER_DRAW_OFFSET, height - BROWSER_DRAW_OFFSET, 0).uv(1.0f, 1.0f).color(255, 255, 255, 255).endVertex();
        buffer.vertex(width - BROWSER_DRAW_OFFSET, BROWSER_DRAW_OFFSET, 0).uv(1.0f, 0.0f).color(255, 255, 255, 255).endVertex();
        buffer.vertex(BROWSER_DRAW_OFFSET, BROWSER_DRAW_OFFSET, 0).uv(0.0f, 0.0f).color(255, 255, 255, 255).endVertex();
        t.end();

        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableDepthTest();

        // 解除帧缓冲绑定，恢复默认帧缓冲区
        framebuffer.unbindWrite();

        // 将帧缓冲内容绘制到屏幕
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        framebuffer.bindRead();
        framebuffer.blitToScreen(this.width, this.height);

        super.render(poseStack, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        mcefRenderer.cleanup();
        setIsRender(false);
        super.onClose();
    }
}
