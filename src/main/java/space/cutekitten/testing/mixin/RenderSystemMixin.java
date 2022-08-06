package space.cutekitten.testing.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.cutekitten.testing.client.ClientDB;

@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {
//    @Inject(method = "getProjectionMatrix", at = @At("RETURN"), cancellable = true)
//    private static void getMatrix(CallbackInfoReturnable<Matrix4f> cir) {
//        if (ClientDB.renderingTest.isOrthographic()) {
//            cir.setReturnValue(ClientDB.getOrthographicMatrix());
//        }
//    }

    @Redirect(method = "enableDepthTest", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_enableDepthTest()V"), remap = false)
    private static void enableDepthTest() {
        if (ClientDB.renderingTest.isDisableDepthTest()) {
            GlStateManager._disableDepthTest();
            return;
        }

        GlStateManager._enableDepthTest();
    }
}
