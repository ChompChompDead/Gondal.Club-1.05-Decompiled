package com.esoterik.client.manager;

import com.esoterik.client.features.*;
import com.esoterik.client.features.command.*;
import com.esoterik.client.*;
import com.esoterik.client.features.command.commands.*;
import java.util.*;
import com.esoterik.client.util.*;

public class CommandManager extends Feature
{
    private String clientMessage;
    private String prefix;
    private ArrayList<Command> commands;
    
    public CommandManager() {
        super("Command");
        this.clientMessage = esohack.getName();
        this.prefix = "-";
        (this.commands = new ArrayList<Command>()).add(new BindCommand());
        this.commands.add(new ModuleCommand());
        this.commands.add(new PrefixCommand());
        this.commands.add(new ConfigCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ReloadCommand());
        this.commands.add(new UnloadCommand());
        this.commands.add(new ReloadSoundCommand());
        this.commands.add(new BookCommand());
        this.commands.add(new CrashCommand());
        this.commands.add(new HistoryCommand());
    }
    
    public void executeCommand(final String command) {
        final String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        final String name = parts[0].substring(1);
        final String[] args = removeElement(parts, 0);
        for (int i = 0; i < args.length; ++i) {
            if (args[i] != null) {
                args[i] = strip(args[i], "\"");
            }
        }
        for (final Command c : this.commands) {
            if (c.getName().equalsIgnoreCase(name)) {
                c.execute(parts);
                return;
            }
        }
        Command.sendMessage("Unknown command. try 'commands' for a list of commands.");
    }
    
    public static String[] removeElement(final String[] input, final int indexToDelete) {
        final List result = new LinkedList();
        for (int i = 0; i < input.length; ++i) {
            if (i != indexToDelete) {
                result.add(input[i]);
            }
        }
        return result.toArray(input);
    }
    
    private static String strip(final String str, final String key) {
        if (str.startsWith(key) && str.endsWith(key)) {
            return str.substring(key.length(), str.length() - key.length());
        }
        return str;
    }
    
    public Command getCommandByName(final String name) {
        for (final Command command : this.commands) {
            if (command.getName().equals(name)) {
                return command;
            }
        }
        return null;
    }
    
    public ArrayList<Command> getCommands() {
        return this.commands;
    }
    
    public String getClientMessage() {
        return this.clientMessage;
    }
    
    public void setClientMessage(final String clientMessage) {
        this.clientMessage = TextUtil.coloredString(clientMessage, TextUtil.Color.DARK_PURPLE);
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}
