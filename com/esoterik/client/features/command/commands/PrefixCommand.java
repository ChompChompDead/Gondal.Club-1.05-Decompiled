package com.esoterik.client.features.command.commands;

import com.esoterik.client.features.command.*;
import com.esoterik.client.features.modules.client.*;
import com.esoterik.client.*;

public class PrefixCommand extends Command
{
    public PrefixCommand() {
        super("prefix", new String[] { "<char>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("§cSpecify a new prefix.");
            return;
        }
        esohack.moduleManager.getModuleByClass(ClickGui.class).prefix.setValue(commands[0]);
        Command.sendMessage("Prefix set to §a" + esohack.commandManager.getPrefix());
    }
}
