package space.cutekitten.testing.mixin;

import net.minecraft.client.render.BufferRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BufferRenderer.class)
public class BufferRendererMixin {
//    @Redirect(method = "drawWithShaderInternal", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getProjectionMatrix()Lnet/minecraft/util/math/Matrix4f;"))
//    private static Matrix4f getMatrix() {
//        return ClientDB.renderingTest.isOrthographic() ? RenderSystem.getModelViewMatrix() : RenderSystem.getProjectionMatrix();
//    }
}
