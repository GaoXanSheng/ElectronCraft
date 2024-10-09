package top.yunmouren.electroncraft.Client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FrameBuffer  extends RenderTarget {
    public FrameBuffer(boolean p_166199_) {
        super(p_166199_);
    }
}
