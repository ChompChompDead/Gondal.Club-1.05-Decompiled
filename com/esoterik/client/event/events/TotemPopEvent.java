package com.esoterik.client.event.events;

import com.esoterik.client.event.*;
import net.minecraft.entity.player.*;

public class TotemPopEvent extends EventStage
{
    private EntityPlayer entity;
    
    public TotemPopEvent(final EntityPlayer entity) {
        this.entity = entity;
    }
    
    public EntityPlayer getEntity() {
        return this.entity;
    }
}
