package com.esoterik.client.features.modules.player;

import com.esoterik.client.features.modules.*;
import com.esoterik.client.features.setting.*;
import com.esoterik.client.features.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import com.esoterik.client.util.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class MCP extends Module
{
    private Setting<Mode> mode;
    private Setting<Boolean> stopRotation;
    private Setting<Integer> rotation;
    private boolean clicked;
    
    public MCP() {
        super("MCP", "Throws a pearl", Category.PLAYER, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.MIDDLECLICK));
        this.stopRotation = (Setting<Boolean>)this.register(new Setting("Rotation", (T)true));
        this.rotation = (Setting<Integer>)this.register(new Setting("Delay", (T)10, (T)0, (T)100, v -> this.stopRotation.getValue()));
        this.clicked = false;
    }
    
    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck() && this.mode.getValue() == Mode.TOGGLE) {
            this.throwPearl();
            this.disable();
        }
    }
    
    @Override
    public void onTick() {
        if (this.mode.getValue() == Mode.MIDDLECLICK) {
            if (Mouse.isButtonDown(2)) {
                if (!this.clicked) {
                    this.throwPearl();
                }
                this.clicked = true;
            }
            else {
                this.clicked = false;
            }
        }
    }
    
    private void throwPearl() {
        final int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        final boolean offhand = MCP.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151079_bi;
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MCP.mc.field_71439_g.field_71071_by.field_70461_c;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            MCP.mc.field_71442_b.func_187101_a((EntityPlayer)MCP.mc.field_71439_g, (World)MCP.mc.field_71441_e, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
    
    public enum Mode
    {
        TOGGLE, 
        MIDDLECLICK;
    }
}
