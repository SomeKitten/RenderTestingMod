package space.cutekitten.testing.mixin;

import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Matrix4f.class)
public interface Matrix4fAccessor {
    @Accessor("a11")
    public void setA11(float a11);
    @Accessor("a11")
    public float getA11();
    @Accessor("a22")
    public void setA22(float a22);
    @Accessor("a22")
    public float getA22();

    @Accessor("a23")
    public void setA23(float a23);
}
