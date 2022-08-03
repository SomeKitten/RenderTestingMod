package space.cutekitten.testing.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class ClientDB {
    public enum RenderingTest {
        NONE,
        LOOK_AT,
        ONE_BLOCK,
        ONE_TYPE,
        FLAT,
        RANDOM_OFFSET,
        RANDOM_SCALE,
        RANDOM_ROTATION,
        RANDOM_INSANITY,
        ORTHOGRAPHIC,
//        WIREFRAME,
        ;

        static {
            ONE_BLOCK.useNoShadow().useNoSideCulling();
            ONE_TYPE.useNoShadow().useNoSideCulling();
            FLAT.useNoShadow().useFullBright();
            RANDOM_OFFSET.useUpdateEveryTick().useNoShadow().useNoSideCulling().useRandomOffset();
            RANDOM_SCALE.useUpdateEveryTick().useNoShadow().useNoSideCulling().useRandomScale();
            RANDOM_ROTATION.useUpdateEveryTick().useNoShadow().useNoSideCulling().useRandomRotation();
            RANDOM_INSANITY.useUpdateEveryTick().useNoShadow().useNoSideCulling().useRandomOffset().useRandomScale().useRandomRotation();
            ORTHOGRAPHIC.useOrthographic();
        }

        private static final RenderingTest[] VALUES = values();
        public RenderingTest previous() {
            int val = (this.ordinal() - 1) % VALUES.length;
            return VALUES[val < 0 ? VALUES.length - 1 : val];
        }
        public RenderingTest next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        private boolean updateEveryTick = false;
        private boolean noShadow = false;
        private boolean fullBright = false;
        private boolean noSideCulling = false;
        private boolean randomOffset = false;
        private boolean randomScale = false;
        private boolean randomRotation = false;
        private boolean orthographic = false;

        public RenderingTest useUpdateEveryTick() {
            updateEveryTick = true;
            return this;
        }
        public RenderingTest useNoShadow() {
            noShadow = true;
            return this;
        }
        public RenderingTest useFullBright() {
            fullBright = true;
            return this;
        }
        public RenderingTest useNoSideCulling() {
            noSideCulling = true;
            return this;
        }
        public RenderingTest useRandomOffset() {
            randomOffset = true;
            return this;
        }
        public RenderingTest useRandomScale() {
            randomScale = true;
            return this;
        }
        public RenderingTest useRandomRotation() {
            randomRotation = true;
            return this;
        }
        public RenderingTest useOrthographic() {
            orthographic = true;
            return this;
        }

        public boolean isUpdateEveryTick() {
            return updateEveryTick;
        }
        public boolean isNoShadow() {
            return noShadow;
        }
        public boolean isFullBright() {
            return fullBright;
        }
        public boolean isNoSideCulling() {
            return noSideCulling;
        }
        public boolean isRandomOffset() {
            return randomOffset;
        }
        public boolean isRandomScale() {
            return randomScale;
        }
        public boolean isRandomRotation() {
            return randomRotation;
        }
        public boolean isOrthographic() {
            return orthographic;
        }
    }
    public static BlockState lookingAtSolidBlock;
    public static BlockPos lookingAtSolidPos;
    public static BlockState lookingAtBlock;
    public static BlockPos lookingAtPos;
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static RenderingTest renderingTest = RenderingTest.NONE;
    public static float renderingIntensity = 0.1f;
    public static Random random = new Random();
}
