package space.cutekitten.testing.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.cutekitten.testing.client.ClientDB;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
//        cycle test keybinding
        if (action == GLFW.GLFW_PRESS && key == InputUtil.GLFW_KEY_B) {
            ClientDB.renderingTest = ClientDB.renderingTest.next();

            ClientDB.client.worldRenderer.reload();
        }
    }
}
