package space.cutekitten.testing.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import space.cutekitten.testing.client.ClientDB;

@Mixin(targets = "net.minecraft.client.render.chunk.ChunkBuilder$BuiltChunk$RebuildTask")
public class RebuildTaskMixin {
    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;")
    )
    private BlockState getBlockState(ChunkRendererRegion instance, BlockPos pos) {
        BlockState state = instance.getBlockState(pos);

        switch (ClientDB.renderingTest) {
            case LOOK_AT -> {
                if (!state.isFullCube(MinecraftClient.getInstance().world, pos)) return state;
                return ClientDB.lookingAtSolidBlock == null ? Blocks.WHITE_CONCRETE.getDefaultState() : ClientDB.lookingAtSolidBlock;
            }
            case ONE_BLOCK -> {
                if (pos.equals(ClientDB.lookingAtPos)) return state;
                return Blocks.AIR.getDefaultState();
            }
            case ONE_TYPE -> {
                if (ClientDB.lookingAtBlock != null && state.getBlock() == ClientDB.lookingAtBlock.getBlock()) return state;
                return Blocks.AIR.getDefaultState();
            }
        }

        return state;
    }
}
