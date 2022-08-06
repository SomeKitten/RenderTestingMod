package space.cutekitten.testing.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import space.cutekitten.testing.client.ClientDB;

@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {
    @Redirect(method = "enableDepthTest", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_enableDepthTest()V"), remap = false)
    private static void enableDepthTest() {
        if (ClientDB.renderingTest.isDisableDepthTest()) {
            GlStateManager._disableDepthTest();
            return;
        }

        GlStateManager._enableDepthTest();
    }
}
