package com.esoterik.client.mixin.mixins;

import net.minecraft.client.model.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.inventory.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.esoterik.client.features.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ LayerBipedArmor.class })
public abstract class MixinLayerBipedArmor extends LayerArmorBase<ModelBiped>
{
    public MixinLayerBipedArmor(final RenderLivingBase<?> rendererIn) {
        super((RenderLivingBase)rendererIn);
    }
    
    @Inject(method = { "setModelSlotVisible" }, at = { @At("HEAD") }, cancellable = true)
    protected void setModelSlotVisible(final ModelBiped model, final EntityEquipmentSlot slotIn, final CallbackInfo info) {
        final NoRender noArmor = NoRender.getInstance();
        if (noArmor.isOn() && noArmor.noArmor.getValue() != NoRender.NoArmor.NONE) {
            info.cancel();
            switch (slotIn) {
                case HEAD: {
                    model.field_78116_c.field_78806_j = false;
                    model.field_178720_f.field_78806_j = false;
                    break;
                }
                case CHEST: {
                    model.field_78115_e.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
                    model.field_178723_h.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
                    model.field_178724_i.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
                    break;
                }
                case LEGS: {
                    model.field_78115_e.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
                    model.field_178721_j.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
                    model.field_178722_k.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
                    break;
                }
                case FEET: {
                    model.field_178721_j.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
                    model.field_178722_k.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
                    break;
                }
            }
        }
    }
}
