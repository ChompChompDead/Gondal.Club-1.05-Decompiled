package com.esoterik.client.event.events;

import com.esoterik.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import com.esoterik.client.features.*;
import com.esoterik.client.features.setting.*;

@Cancelable
public class ClientEvent extends EventStage
{
    private Feature feature;
    private Setting setting;
    
    public ClientEvent(final int stage, final Feature feature) {
        super(stage);
        this.feature = feature;
    }
    
    public ClientEvent(final Setting setting) {
        super(2);
        this.setting = setting;
    }
    
    public Feature getFeature() {
        return this.feature;
    }
    
    public Setting getSetting() {
        return this.setting;
    }
}
