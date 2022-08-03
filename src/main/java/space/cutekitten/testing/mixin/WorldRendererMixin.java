package space.cutekitten.testing.mixin;

import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import space.cutekitten.testing.client.ClientDB;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Redirect(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlUniform;set(Lnet/minecraft/util/math/Matrix4f;)V", ordinal = 1))
    private void getPositionMatrix(GlUniform instance, Matrix4f values) {
        if (ClientDB.renderingTest.isOrthographic()) {
            instance.set(ClientDB.getOrthographicMatrix());
            return;
        }

        instance.set(values);
    }
}
