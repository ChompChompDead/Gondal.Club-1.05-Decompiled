package com.esoterik.client.manager;

import com.esoterik.client.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class InventoryManager implements Util
{
    private int recoverySlot;
    
    public InventoryManager() {
        this.recoverySlot = -1;
    }
    
    public void update() {
        if (this.recoverySlot != -1) {
            InventoryManager.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange((this.recoverySlot == 8) ? 7 : (this.recoverySlot + 1)));
            InventoryManager.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.recoverySlot));
            InventoryManager.mc.field_71439_g.field_71071_by.field_70461_c = this.recoverySlot;
            InventoryManager.mc.field_71442_b.func_78750_j();
            this.recoverySlot = -1;
        }
    }
    
    public void recoverSilent(final int slot) {
        this.recoverySlot = slot;
    }
}
