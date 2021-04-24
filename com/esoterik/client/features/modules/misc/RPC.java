package com.esoterik.client.features.modules.misc;

import com.esoterik.client.features.modules.*;
import com.esoterik.client.*;

public class RPC extends Module
{
    public static RPC INSTANCE;
    
    public RPC() {
        super("RPC", "Discord rich presence", Category.CLIENT, false, false, false);
        RPC.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        Discord.start();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Discord.stop();
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        Discord.start();
    }
}
