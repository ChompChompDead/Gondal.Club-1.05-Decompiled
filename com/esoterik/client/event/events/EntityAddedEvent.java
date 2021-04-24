package com.esoterik.client.event.events;

import com.esoterik.client.event.*;
import net.minecraft.entity.*;

public class EntityAddedEvent extends EventStage
{
    private Entity entity;
    
    public EntityAddedEvent(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
