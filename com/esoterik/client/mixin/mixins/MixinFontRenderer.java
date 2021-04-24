package com.esoterik.client.mixin.mixins;

import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ FontRenderer.class })
public abstract class MixinFontRenderer
{
    @Shadow
    public abstract int func_180455_b(final String p0, final float p1, final float p2, final int p3, final boolean p4);
    
    @Redirect(method = { "drawString(Ljava/lang/String;FFIZ)I" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I"))
    public int renderStringHook(final FontRenderer fontrenderer, final String text, final float x, final float y, final int color, final boolean dropShadow) {
        return this.func_180455_b(text, x, y, color, dropShadow);
    }
}
