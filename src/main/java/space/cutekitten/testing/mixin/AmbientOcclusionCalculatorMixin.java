package space.cutekitten.testing.mixin;

import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.cutekitten.testing.client.ClientDB;

@Mixin(targets = "net.minecraft.client.render.block.BlockModelRenderer$AmbientOcclusionCalculator")
public class AmbientOcclusionCalculatorMixin {
    @Inject(method = "getAmbientOcclusionBrightness", at = @At("RETURN"), cancellable = true)
    private void getAmbientOcclusionBrightness(int i, int j, int k, int l, CallbackInfoReturnable<Integer> cir) {
        if (ClientDB.renderingTest.isNoShadow()) {
            cir.setReturnValue(0x000000FF);
        }
    }

    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"))
    private float getBrightness(BlockRenderView instance, Direction direction, boolean b) {
        if (ClientDB.renderingTest.isFullBright()) {
            return 1.0F;
        }

        return instance.getBrightness(direction, b);
    }

    @ModifyVariable(method = "apply", at = @At("STORE"), ordinal = 9)
    private float modifyBrightness0(float f) {
        if (ClientDB.renderingTest.isNoShadow()) {
            return 1.0F;
        }

        return f;
    }
    @ModifyVariable(method = "apply", at = @At("STORE"), ordinal = 10)
    private float modifyBrightness1(float f) {
        if (ClientDB.renderingTest.isNoShadow()) {
            return 1.0F;
        }

        return f;
    }
    @ModifyVariable(method = "apply", at = @At("STORE"), ordinal = 11)
    private float modifyBrightness2(float f) {
        if (ClientDB.renderingTest.isNoShadow()) {
            return 1.0F;
        }

        return f;
    }
    @ModifyVariable(method = "apply", at = @At("STORE"), ordinal = 12)
    private float modifyBrightness3(float f) {
        if (ClientDB.renderingTest.isNoShadow()) {
            return 1.0F;
        }

        return f;
    }
}
