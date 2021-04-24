package com.esoterik.client.features.command.commands;

import com.esoterik.client.features.command.*;
import com.esoterik.client.*;

public class UnloadCommand extends Command
{
    public UnloadCommand() {
        super("unload", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        esohack.unload(true);
    }
}
