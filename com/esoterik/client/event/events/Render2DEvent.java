package com.esoterik.client.event.events;

import com.esoterik.client.event.*;
import net.minecraft.client.gui.*;

public class Render2DEvent extends EventStage
{
    public float partialTicks;
    public ScaledResolution scaledResolution;
    
    public Render2DEvent(final float partialTicks, final ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }
    
    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public void setScaledResolution(final ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
    
    public double getScreenWidth() {
        return this.scaledResolution.func_78327_c();
    }
    
    public double getScreenHeight() {
        return this.scaledResolution.func_78324_d();
    }
}
