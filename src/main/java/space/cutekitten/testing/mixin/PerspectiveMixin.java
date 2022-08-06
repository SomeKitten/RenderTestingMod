package space.cutekitten.testing.mixin;

import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.cutekitten.testing.client.ClientDB;

@Mixin(Perspective.class)
public class PerspectiveMixin {
    @Inject(method = "next", at = @At("RETURN"), cancellable = true)
    private void next(CallbackInfoReturnable<Perspective> cir) {
        if (ClientDB.renderingTest.isOrthographic()) {
            cir.setReturnValue(Perspective.THIRD_PERSON_BACK);
        }
    }
}
