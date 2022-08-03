package space.cutekitten.testing.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.cutekitten.testing.client.ClientDB;
import space.cutekitten.testing.client.Raycast;

import java.nio.FloatBuffer;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public abstract Window getWindow();

    @Inject(at = @At("RETURN"), method = "tick")
    private void onEndTick(CallbackInfo info) {
        if (!ClientDB.hasOrthographicMatrix()) {
            Matrix4f orthographic = new Matrix4f();

            float width = getWindow().getFramebufferWidth();
            float height = getWindow().getFramebufferHeight();
            float scale = 0.2f;

            orthographic.readRowMajor(FloatBuffer.wrap(new float[]{
                    scale, 0.0f, 0.0f, 0.0f,
                    0.0f, scale * (width / height), 0.0f, 0.0f,
                    0.0f, 0.0f, -2.0E-5f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f
            }));

            ClientDB.setOrthographicMatrix(orthographic);
        }

        updateLookingAtSolid();
        updateLookingAt();

        if (ClientDB.renderingTest.isUpdateEveryTick()) {
            MinecraftClient client = ClientDB.client;
            ClientPlayerEntity player = client.player;
            ClientWorld world = client.world;
            if (player == null || world == null) {
                return;
            }

            BlockPos pos = player.getBlockPos();
            int radius = client.options.getClampedViewDistance() * 8 + 8;

            client.worldRenderer.scheduleBlockRenders(
                    pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                    pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
            );
        }
    }

    private void updateLookingAtSolid() {
        MinecraftClient client = ClientDB.client;
        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;
        if (player == null || world == null) {
            ClientDB.lookingAtSolidBlock = null;
            return;
        }

        HitResult result = Raycast.playerRaycast(1000.0D, client.getTickDelta(), false);

        if (!(result instanceof BlockHitResult blockHitResult)) {
            ClientDB.lookingAtSolidPos = new BlockPos(result.getPos());
            ClientDB.lookingAtSolidBlock = null;
            return;
        }

        ClientDB.lookingAtSolidPos = blockHitResult.getBlockPos();

        BlockState lookingAt = client.world.getBlockState(blockHitResult.getBlockPos());
        if (lookingAt.isAir()) {
            ClientDB.lookingAtSolidBlock = null;
            return;
        }

        ClientDB.lookingAtSolidBlock = lookingAt;
    }

    private void updateLookingAt() {
        MinecraftClient client = ClientDB.client;
        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;
        if (player == null || world == null) {
            ClientDB.lookingAtBlock = null;
            return;
        }

        HitResult result = player.raycast(1000.0D, client.getTickDelta(), false);

        BlockPos lookingAtPosOld = ClientDB.lookingAtPos;

        if (!(result instanceof BlockHitResult blockHitResult)) {
            ClientDB.lookingAtPos = new BlockPos(result.getPos());
            ClientDB.lookingAtBlock = null;
            return;
        }

        ClientDB.lookingAtPos = blockHitResult.getBlockPos();

        if (!ClientDB.lookingAtPos.equals(lookingAtPosOld)) {
            int radius = client.options.getClampedViewDistance() * 8 + 8;
            BlockPos pos = client.player.getBlockPos();
            client.worldRenderer.scheduleBlockRenders(
                    pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                    pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
            );
        }

        BlockState lookingAt = client.world.getBlockState(blockHitResult.getBlockPos());
        if (lookingAt.isAir()) {
            ClientDB.lookingAtBlock = null;
            return;
        }

        ClientDB.lookingAtBlock = lookingAt;
    }
}
