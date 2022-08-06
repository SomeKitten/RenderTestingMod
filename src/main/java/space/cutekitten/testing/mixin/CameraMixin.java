package space.cutekitten.testing.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.cutekitten.testing.client.ClientDB;

@Mixin(Camera.class)
public abstract class CameraMixin {
//    @Shadow protected abstract void setPos(Vec3d pos);
//
//    @Shadow private Vec3d pos;
//
//    @Inject(method = "update", at = @At("RETURN"))
//    private void offset(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
//        if (!ClientDB.client.options.getPerspective().isFirstPerson()) {
//            Vec3d offset = ClientDB.client.player.getRotationVec(tickDelta).multiply(-5);
//            setPos(new Vec3d(pos.x + offset.x, pos.y + offset.y, pos.z + offset.z));
//        }
//    }
}
