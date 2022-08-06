package space.cutekitten.testing.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import space.cutekitten.testing.client.ClientDB;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack$Entry;getPositionMatrix()Lnet/minecraft/util/math/Matrix4f;", ordinal = 1))
    private Matrix4f getMatrix(MatrixStack.Entry entry) {
        if (ClientDB.renderingTest.isOrthographic()) {
            return ClientDB.getOrthographicMatrix();
        }
        return entry.getPositionMatrix();
    }
}
