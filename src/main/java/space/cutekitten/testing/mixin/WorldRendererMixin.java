package space.cutekitten.testing.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.cutekitten.testing.client.ClientDB;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
//    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
//    private Camera modifyCamera(Camera camera) {
//        camera.getPos().add(ClientDB.client.player.getRotationVec(ClientDB.client.getTickDelta()).multiply(-100));
//        return camera;
//    }

//    TODO: fix hotbar and inventories
//    @Redirect(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlUniform;set(Lnet/minecraft/util/math/Matrix4f;)V", ordinal = 1))
//    private void getPositionMatrix(GlUniform instance, Matrix4f values) {
//        if (ClientDB.renderingTest.isOrthographic()) {
//            instance.set(ClientDB.getOrthographicMatrix());
//            return;
//        }
//
//        instance.set(values);
//    }

    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlUniform;set(Lnet/minecraft/util/math/Matrix4f;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void getPositionMatrix(RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix, CallbackInfo ci) {
        if (ClientDB.renderingTest.isOrthographic()) {
            RenderSystem.getShader().projectionMat.set(ClientDB.getOrthographicMatrix());
        }
    }

//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiplyPositionMatrix(Lnet/minecraft/util/math/Matrix4f;)V"))
//    private void wtf(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
//        if (ClientDB.renderingTest.isOrthographic()) {
//            matrices.peek().getPositionMatrix().load(ClientDB.getOrthographicMatrix());
//        }
//    }

//    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDDLnet/minecraft/util/math/Matrix4f;)V"))
//    private Matrix4f modifyPositionMatrix(Matrix4f original) {
//        if (ClientDB.renderingTest.isOrthographic()) {
//            return ClientDB.getOrthographicMatrix();
//        }
//
//        return original;
//    }
}
