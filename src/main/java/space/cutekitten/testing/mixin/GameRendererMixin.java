package space.cutekitten.testing.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import space.cutekitten.testing.client.ClientDB;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
//    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Matrix4f;projectionMatrix(FFFFFF)Lnet/minecraft/util/math/Matrix4f;"))
//    private Matrix4f getMatrix(float left, float right, float bottom, float top, float nearPlane, float farPlane) {
//        if (ClientDB.renderingTest.isOrthographic()) {
//            return ClientDB.getOrthographicMatrix();
//        }
//
//        return Matrix4f.projectionMatrix(left, right, bottom, top, nearPlane, farPlane);
//    }

//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void moveHUD(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int i, int j, Window window, Matrix4f matrix4f, MatrixStack matrixStack) {
//        if (ClientDB.renderingTest.isOrthographic()) {
//            float width = (float)((double)ClientDB.client.getWindow().getFramebufferWidth() / ClientDB.client.getWindow().getScaleFactor());
//            float height = (float)((double)ClientDB.client.getWindow().getFramebufferHeight() / ClientDB.client.getWindow().getScaleFactor());
//            float scale = (float) (0.01 * ClientDB.client.getWindow().getScaleFactor());
//            matrixStack.scale(scale, scale, 1);
//            matrixStack.multiply(ClientDB.halfTurn);
//            matrixStack.translate(-width / 2, -height / 2, 0);
//        }
//    }

//    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"))
//    private void flip(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
//        if (ClientDB.renderingTest.isOrthographic()) {
////            matrices.multiply(ClientDB.halfTurn);
//            matrices.scale(0, -1, 0);
//        }
//    }

    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack$Entry;getPositionMatrix()Lnet/minecraft/util/math/Matrix4f;", ordinal = 1))
    private Matrix4f getMatrix(MatrixStack.Entry entry) {
        if (ClientDB.renderingTest.isOrthographic()) {
            return ClientDB.getOrthographicMatrix();
        }
        return entry.getPositionMatrix();
    }

//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void getMatrix(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int i, int j, Window window, Matrix4f matrix4f, MatrixStack matrixStack, MatrixStack matrixStack2) {
//        if (ClientDB.renderingTest.isOrthographic()) {
//            matrixStack2.multiply(ClientDB.halfTurn);
//        }
//    }
}
