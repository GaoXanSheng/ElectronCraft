package top.yunmouren.electroncraft.mixin;

import com.mojang.blaze3d.platform.Window;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = Window.class,remap = true)
public class NoFullscreen {
    @Inject(method = "toggleFullScreen", at = @At("HEAD"), cancellable = true)
    private void onToggleFullScreen(@NotNull CallbackInfo ci) {
        // Skip the original method implementation
        ci.cancel();
    }
}
