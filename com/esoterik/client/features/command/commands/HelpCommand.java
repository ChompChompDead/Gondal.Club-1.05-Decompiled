package com.esoterik.client.features.command.commands;

import com.esoterik.client.features.command.*;
import com.esoterik.client.*;
import java.util.*;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("commands");
    }
    
    @Override
    public void execute(final String[] commands) {
        Command.sendMessage("You can use following commands: ");
        for (final Command command : esohack.commandManager.getCommands()) {
            Command.sendMessage(esohack.commandManager.getPrefix() + command.getName());
        }
    }
}
