package com.esoterik.client.features.modules.render;

import com.esoterik.client.features.modules.*;
import com.esoterik.client.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import com.esoterik.client.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Fullbright extends Module
{
    public Setting<Mode> mode;
    public Setting<Boolean> effects;
    private float previousSetting;
    
    public Fullbright() {
        super("Fullbright", "Makes your game brighter.", Category.RENDER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.GAMMA));
        this.effects = (Setting<Boolean>)this.register(new Setting("Effects", (T)false));
        this.previousSetting = 1.0f;
    }
    
    @Override
    public void onEnable() {
        this.previousSetting = Fullbright.mc.field_71474_y.field_74333_Y;
    }
    
    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.GAMMA) {
            Fullbright.mc.field_71474_y.field_74333_Y = 1000.0f;
        }
        if (this.mode.getValue() == Mode.POTION) {
            Fullbright.mc.field_71439_g.func_70690_d(new PotionEffect(MobEffects.field_76439_r, 5210));
        }
    }
    
    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.POTION) {
            Fullbright.mc.field_71439_g.func_184589_d(MobEffects.field_76439_r);
        }
        Fullbright.mc.field_71474_y.field_74333_Y = this.previousSetting;
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && event.getPacket() instanceof SPacketEntityEffect && this.effects.getValue()) {
            final SPacketEntityEffect packet = event.getPacket();
            if (Fullbright.mc.field_71439_g != null && packet.func_149426_d() == Fullbright.mc.field_71439_g.func_145782_y() && (packet.func_149427_e() == 9 || packet.func_149427_e() == 15)) {
                event.setCanceled(true);
            }
        }
    }
    
    public enum Mode
    {
        GAMMA, 
        POTION;
    }
}
