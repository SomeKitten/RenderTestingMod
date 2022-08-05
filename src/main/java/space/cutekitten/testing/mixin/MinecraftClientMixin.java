package space.cutekitten.testing.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.debug.BeeDebugRenderer;
import net.minecraft.client.render.debug.NeighborUpdateDebugRenderer;
import net.minecraft.client.render.debug.VillageDebugRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.cutekitten.testing.client.ClientDB;
import space.cutekitten.testing.client.Raycast;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public abstract Window getWindow();

    @Inject(at = @At("RETURN"), method = "tick")
    private void onEndTick(CallbackInfo info) {
        loadDebugInfo();

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

    private void loadDebugInfo() {
        Object[] paths = ClientDB.newPaths.toArray();
        for (Object pathParams : paths) {
            ImmutableTriple<Integer, Path, Float> path = (ImmutableTriple<Integer, Path, Float>) pathParams;
//            sometimes its null???
            if (path == null) {
                continue;
            }
            ClientDB.client.debugRenderer.pathfindingDebugRenderer.addPath(path.getLeft(), path.getMiddle(), path.getRight());
            ClientDB.newPaths.remove(path);
        }


        Object[] neighborUpdates = ClientDB.newNeighborUpdates.toArray();
        for (Object neighborUpdate : neighborUpdates) {
            Pair<Long, BlockPos> update = (Pair<Long, BlockPos>) neighborUpdate;
//            sometimes its null???
            if (update == null) {
                continue;
            }
            ((NeighborUpdateDebugRenderer)(ClientDB.client.debugRenderer.neighborUpdateDebugRenderer)).addNeighborUpdate(update.getLeft(), update.getRight());
            ClientDB.newNeighborUpdates.remove(update);
        }


        Object[] structures = ClientDB.newStructures.toArray();
        for (Object structure : structures) {
            Pair< StructureWorldAccess, StructureStart> structureStartPair = (Pair< StructureWorldAccess, StructureStart>) structure;
//            sometimes its null???
            if (structureStartPair == null) {
                continue;
            }
            StructureStart structureStart = structureStartPair.getRight();
            List<BlockBox> childrenBoxes = new ArrayList<>();
            List<Boolean> someColorBools = new ArrayList<>();
            for (StructurePiece child : structureStart.getChildren()) {
                childrenBoxes.add(child.getBoundingBox());
                someColorBools.add(true);
            }
            ClientDB.client.debugRenderer.structureDebugRenderer.addStructure(structureStart.getBoundingBox(), childrenBoxes, someColorBools, structureStartPair.getLeft().getDimension());
            ClientDB.newStructures.remove(structure);
        }


        Object[] pois = ClientDB.newPOIs.toArray();
        for (Object poi : pois) {
            Pair<ServerWorld, BlockPos> poiPair = (Pair<ServerWorld, BlockPos>) poi;
//            sometimes its null???
            if (poiPair == null) {
                continue;
            }
            ClientDB.client.debugRenderer.villageDebugRenderer.addPointOfInterest(new VillageDebugRenderer.PointOfInterest(poiPair.getRight(), "POI!", 69));
            ClientDB.newPOIs.remove(poi);
        }


        Object[] bees = ClientDB.newBees.toArray();
        for (Object b : bees) {
            BeeEntity bee = (BeeEntity) b;
//            sometimes its null???
            if (bee == null) {
                continue;
            }
            ClientDB.client.debugRenderer.beeDebugRenderer.addBee(new BeeDebugRenderer.Bee(bee.getUuid(), bee.getId(), bee.getPos(), bee.getNavigation().getCurrentPath(), bee.getHivePos(), bee.getFlowerPos(), 0));
            ClientDB.newBees.remove(bee);
        }

        Object[] beehives = ClientDB.newBeehives.toArray();
        for (Object b : beehives) {
            BeehiveBlockEntity beehive = (BeehiveBlockEntity) b;
//            sometimes its null???
            if (beehive == null) {
                continue;
            }
            ClientDB.client.debugRenderer.beeDebugRenderer.addHive(new BeeDebugRenderer.Hive(beehive.getPos(), "Hive!", beehive.getBeeCount(), BeehiveBlockEntity.getHoneyLevel(beehive.getCachedState()), beehive.isSmoked(), 420));
            ClientDB.newBeehives.remove(beehive);
        }

//        Object[] goals = ClientDB.newGoals.toArray();
//        for (Object g : goals) {
//            GoalSelectorDebugRenderer.GoalSelector goalPair = (GoalSelectorDebugRenderer.GoalSelector) g;
////            sometimes its null???
//            if (goalPair == null) {
//                continue;
//            }
//            ClientDB.client.debugRenderer.goalSelectorDebugRenderer.setGoalSelectorList(goalPair.getGoal(), goalPair.getPriority());
//            ClientDB.newGoals.remove(goalPair);
//        }

        Object[] event = ClientDB.newGameEvents.toArray();
        for (Object e : event) {
            Pair<GameEvent, Vec3d> gameEventPair = (Pair<GameEvent, Vec3d>) e;
//            sometimes its null???
            if (gameEventPair == null) {
                continue;
            }
            ClientDB.client.debugRenderer.gameEventDebugRenderer.addEvent(gameEventPair.getLeft(), gameEventPair.getRight());
            ClientDB.newGameEvents.remove(gameEventPair);
        }

        Object[] eventListener = ClientDB.newGameEvents.toArray();
        for (Object e : eventListener) {
            GameEventListener gameEventListener = (GameEventListener) e;
//            sometimes its null???
            if (gameEventListener == null) {
                continue;
            }
            ClientDB.client.debugRenderer.gameEventDebugRenderer.addListener(gameEventListener.getPositionSource(), gameEventListener.getRange());
            ClientDB.newGameEventListeners.remove(e);
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
