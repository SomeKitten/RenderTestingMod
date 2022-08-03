package space.cutekitten.testing.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.cutekitten.testing.client.ClientDB;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {
    @Inject(method = "render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLnet/minecraft/util/math/random/Random;JI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"))
    private void random(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, CallbackInfo ci) {
        if (ClientDB.renderingTest.isRandomOffset()) {
            float translationRange = ClientDB.renderingIntensity;
            matrices.translate(ClientDB.random.nextDouble() * translationRange - translationRange * 0.5, ClientDB.random.nextDouble() * translationRange - translationRange * 0.5, ClientDB.random.nextDouble() * translationRange - translationRange * 0.5);
        }

        if (ClientDB.renderingTest.isRandomScale()) {
            float scaleRange = ClientDB.renderingIntensity;
            float x = ClientDB.random.nextFloat() * scaleRange + 1 - scaleRange;
            float y = ClientDB.random.nextFloat() * scaleRange + 1 - scaleRange;
            float z = ClientDB.random.nextFloat() * scaleRange + 1 - scaleRange;
            matrices.translate( 0.5, 0.5, 0.5);
            matrices.scale(x, y, z);
            matrices.translate( -0.5, -0.5, -0.5);
        }

        if (ClientDB.renderingTest.isRandomRotation()) {
            Quaternion rotation = Quaternion.fromEulerXyz(
                    (float) (ClientDB.random.nextFloat() * Math.PI * ClientDB.renderingIntensity),
                    (float) (ClientDB.random.nextFloat() * Math.PI * ClientDB.renderingIntensity),
                    (float) (ClientDB.random.nextFloat() * Math.PI * ClientDB.renderingIntensity)
            );

            matrices.translate( 0.5, 0.5, 0.5);
            matrices.multiply(rotation);
            matrices.translate( -0.5, -0.5, -0.5);
        }
    }
}
