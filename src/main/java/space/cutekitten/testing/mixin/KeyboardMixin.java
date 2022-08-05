package space.cutekitten.testing.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
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
        if (action == GLFW.GLFW_PRESS) {
            MinecraftClient client = ClientDB.client;
            switch (key) {
//                cycle effect
                case GLFW.GLFW_KEY_RIGHT -> {
                    ClientDB.renderingTest = ClientDB.renderingTest.next();
                    ClientDB.client.worldRenderer.reload();
                    client.inGameHud.setOverlayMessage(Text.literal("Rendering test: " + ClientDB.renderingTest.name()), false);
                }
                case GLFW.GLFW_KEY_LEFT -> {
                    ClientDB.renderingTest = ClientDB.renderingTest.previous();
                    ClientDB.client.worldRenderer.reload();
                    client.inGameHud.setOverlayMessage(Text.literal("Rendering test: " + ClientDB.renderingTest.name()), false);
                }
//                raise/lower intensity
                case GLFW.GLFW_KEY_UP -> {
                    ClientDB.renderingIntensity += 0.1f;
                    client.inGameHud.setOverlayMessage(Text.literal("Rendering intensity: " + ClientDB.renderingIntensity), false);
                }
                case GLFW.GLFW_KEY_DOWN -> {
                    ClientDB.renderingIntensity -= 0.1f;
                    client.inGameHud.setOverlayMessage(Text.literal("Rendering intensity: " + ClientDB.renderingIntensity), false);
                }
            }
        }
    }
}
