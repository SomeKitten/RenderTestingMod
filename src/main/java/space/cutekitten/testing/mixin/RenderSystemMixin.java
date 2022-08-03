package space.cutekitten.testing.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.cutekitten.testing.client.ClientDB;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Shadow private static Matrix4f modelViewMatrix;

    @Shadow private static Matrix4f projectionMatrix;

    @Inject(method = "getProjectionMatrix", at = @At("RETURN"), cancellable = true)
    private static void getMatrix(CallbackInfoReturnable<Matrix4f> cir) {
        cir.setReturnValue(ClientDB.renderingTest.isOrthographic() ? modelViewMatrix : projectionMatrix);
    }
}
