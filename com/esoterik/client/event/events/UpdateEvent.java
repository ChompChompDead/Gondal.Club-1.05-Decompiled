package com.esoterik.client.event.events;

import com.esoterik.client.event.*;

public class UpdateEvent extends EventStage
{
    private final int stage;
    
    public UpdateEvent(final int stage) {
        this.stage = stage;
    }
    
    @Override
    public final int getStage() {
        return this.stage;
    }
}
