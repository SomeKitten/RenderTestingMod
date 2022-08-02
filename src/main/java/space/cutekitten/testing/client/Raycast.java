package space.cutekitten.testing.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Raycast {
//    raycast from a player's point of view
    public static HitResult playerRaycast(double maxDistance, float tickDelta, boolean includeFluids) {
        ClientPlayerEntity player = ClientDB.client.player;
        Vec3d vec3d = player.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = player.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, player));
    }

//    generic raycast
    public static BlockHitResult raycast(RaycastContext context) {
        return raycast(
            context.getStart(),
            context.getEnd(),
            context,
//                block hit factory
            (contextx, pos) -> {
                BlockState blockState = ClientDB.client.world.getBlockState(pos);
                FluidState fluidState = ClientDB.client.world.getFluidState(pos);

                Vec3d vec3d = contextx.getStart();
                Vec3d vec3d2 = contextx.getEnd();

                VoxelShape voxelShape = contextx.getBlockShape(blockState, ClientDB.client.world, pos);
                BlockHitResult blockHitResult = ClientDB.client.world.raycastBlock(vec3d, vec3d2, pos, voxelShape, blockState);

                VoxelShape voxelShape2 = contextx.getFluidShape(fluidState, ClientDB.client.world, pos);
                BlockHitResult blockHitResult2 = voxelShape2.raycast(vec3d, vec3d2, pos);

                double d = blockHitResult == null ? Double.MAX_VALUE : contextx.getStart().squaredDistanceTo(blockHitResult.getPos());
                double e = blockHitResult2 == null ? Double.MAX_VALUE : contextx.getStart().squaredDistanceTo(blockHitResult2.getPos());

                return d <= e ? blockHitResult : blockHitResult2;
            },
//                miss factory
            (contextx) -> {
                Vec3d vec3d = contextx.getStart().subtract(contextx.getEnd());
                return BlockHitResult.createMissed(contextx.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), new BlockPos(contextx.getEnd()));
            },
//                filter
            (blockHitResult) -> {
                if (blockHitResult == null) return false;

                BlockState blockState = ClientDB.client.world.getBlockState(blockHitResult.getBlockPos());

                return blockState.isFullCube(ClientDB.client.world, blockHitResult.getBlockPos());
            }
        );
    }

//    generic inner raycast function
    static <T, C> T raycast(Vec3d start, Vec3d end, C context, BiFunction<C, BlockPos, T> blockHitFactory, Function<C, T> missFactory, Function<T, Boolean> filter) {
        if (start.equals(end)) {
            return missFactory.apply(context);
        } else {
            double d = MathHelper.lerp(-1.0E-7, end.x, start.x);
            double e = MathHelper.lerp(-1.0E-7, end.y, start.y);
            double f = MathHelper.lerp(-1.0E-7, end.z, start.z);
            double g = MathHelper.lerp(-1.0E-7, start.x, end.x);
            double h = MathHelper.lerp(-1.0E-7, start.y, end.y);
            double i = MathHelper.lerp(-1.0E-7, start.z, end.z);

            int j = MathHelper.floor(g);
            int k = MathHelper.floor(h);
            int l = MathHelper.floor(i);

            BlockPos.Mutable mutable = new BlockPos.Mutable(j, k, l);

            T object = blockHitFactory.apply(context, mutable);
            object = filter.apply(object) ? object : null;

            if (object != null) {
                return object;
            } else {
                double m = d - g;
                double n = e - h;
                double o = f - i;

                int p = MathHelper.sign(m);
                int q = MathHelper.sign(n);
                int r = MathHelper.sign(o);

                double s = p == 0 ? Double.MAX_VALUE : (double)p / m;
                double t = q == 0 ? Double.MAX_VALUE : (double)q / n;
                double u = r == 0 ? Double.MAX_VALUE : (double)r / o;

                double v = s * (p > 0 ? 1.0 - MathHelper.fractionalPart(g) : MathHelper.fractionalPart(g));
                double w = t * (q > 0 ? 1.0 - MathHelper.fractionalPart(h) : MathHelper.fractionalPart(h));
                double x = u * (r > 0 ? 1.0 - MathHelper.fractionalPart(i) : MathHelper.fractionalPart(i));

                T object2;
                do {
                    if (!(v <= 1.0) && !(w <= 1.0) && !(x <= 1.0)) {
                        return missFactory.apply(context);
                    }

                    if (v < w) {
                        if (v < x) {
                            j += p;
                            v += s;
                        } else {
                            l += r;
                            x += u;
                        }
                    } else if (w < x) {
                        k += q;
                        w += t;
                    } else {
                        l += r;
                        x += u;
                    }

                    object2 = blockHitFactory.apply(context, mutable.set(j, k, l));
                    object2 = filter.apply(object2) ? object2 : null;
                } while(object2 == null);

                return object2;
            }
        }
    }
}
