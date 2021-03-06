package com.esoterik.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.block.model.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.esoterik.client.features.modules.render.*;
import net.minecraft.client.*;
import com.esoterik.client.features.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RenderItem.class })
public abstract class MixinItemRenderer
{
    @Inject(method = { "renderItemModel" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.BEFORE) })
    private void test(final ItemStack stack, final IBakedModel bakedmodel, final ItemCameraTransforms.TransformType transform, final boolean leftHanded, final CallbackInfo ci) {
        if (ViewModel.getINSTANCE().enabled.getValue() && Minecraft.func_71410_x().field_71474_y.field_74320_O == 0 && !Feature.fullNullCheck()) {
            GlStateManager.func_179152_a((float)ViewModel.getINSTANCE().sizeX.getValue(), (float)ViewModel.getINSTANCE().sizeY.getValue(), (float)ViewModel.getINSTANCE().sizeZ.getValue());
            GlStateManager.func_179114_b(ViewModel.getINSTANCE().rotationX.getValue() * 360.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.func_179114_b(ViewModel.getINSTANCE().rotationY.getValue() * 360.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.func_179114_b(ViewModel.getINSTANCE().rotationZ.getValue() * 360.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.func_179109_b((float)ViewModel.getINSTANCE().positionX.getValue(), (float)ViewModel.getINSTANCE().positionY.getValue(), (float)ViewModel.getINSTANCE().positionZ.getValue());
        }
    }
}
