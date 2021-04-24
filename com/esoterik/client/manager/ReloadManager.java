package com.esoterik.client.manager;

import com.esoterik.client.features.*;
import net.minecraftforge.common.*;
import com.esoterik.client.features.command.*;
import com.esoterik.client.event.events.*;
import net.minecraft.network.play.client.*;
import com.esoterik.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class ReloadManager extends Feature
{
    public String prefix;
    
    public void init(final String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register((Object)this);
        if (!Feature.fullNullCheck()) {
            Command.sendMessage("§cPhobos has been unloaded. Type " + prefix + "reload to reload.");
        }
    }
    
    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = event.getPacket();
            if (packet.func_149439_c().startsWith(this.prefix) && packet.func_149439_c().contains("reload")) {
                esohack.load();
                event.setCanceled(true);
            }
        }
    }
}
