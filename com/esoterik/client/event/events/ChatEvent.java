package com.esoterik.client.event.events;

import com.esoterik.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Cancelable
public class ChatEvent extends EventStage
{
    private final String msg;
    
    public ChatEvent(final String msg) {
        this.msg = msg;
    }
    
    public String getMsg() {
        return this.msg;
    }
}
