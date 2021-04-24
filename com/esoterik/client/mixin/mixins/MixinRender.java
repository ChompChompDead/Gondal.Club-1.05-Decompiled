package com.esoterik.client.mixin.mixins;

import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.culling.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ Render.class })
public class MixinRender<T extends Entity>
{
    @Inject(method = { "shouldRender" }, at = { @At("HEAD") }, cancellable = true)
    public void shouldRender(final T livingEntity, final ICamera camera, final double camX, final double camY, final double camZ, final CallbackInfoReturnable<Boolean> info) {
        if (livingEntity == null || camera == null || livingEntity.func_184177_bl() == null) {
            info.setReturnValue(false);
        }
    }
}
