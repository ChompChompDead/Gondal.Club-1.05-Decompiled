package com.esoterik.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import com.esoterik.client.features.modules.render.*;
import com.esoterik.client.event.events.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T>
{
    public MixinRenderLivingBase(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn);
    }
    
    @Redirect(method = { "renderModel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelHook(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (Skeleton.getInstance().isEnabled() || ESP.getInstance().isEnabled()) {
            final RenderEntityModelEvent event = new RenderEntityModelEvent(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (Skeleton.getInstance().isEnabled()) {
                Skeleton.getInstance().onRenderModel(event);
            }
            if (ESP.getInstance().isEnabled()) {
                ESP.getInstance().onRenderModel(event);
                if (event.isCanceled()) {
                    return;
                }
            }
        }
        modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
    
    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    public void doRenderPre(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
    }
    
    @Inject(method = { "doRender" }, at = { @At("RETURN") })
    public void doRenderPost(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
    }
}
