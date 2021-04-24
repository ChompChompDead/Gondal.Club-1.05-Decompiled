package com.esoterik.client.features.command.commands;

import com.esoterik.client.features.command.*;
import com.esoterik.client.*;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        esohack.reload();
    }
}
