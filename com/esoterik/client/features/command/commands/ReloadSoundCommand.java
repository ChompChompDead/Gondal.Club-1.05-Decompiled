package com.esoterik.client.features.command.commands;

import com.esoterik.client.features.command.*;
import net.minecraft.client.audio.*;
import net.minecraftforge.fml.common.*;

public class ReloadSoundCommand extends Command
{
    public ReloadSoundCommand() {
        super("sound", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        try {
            final SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue((Class)SoundHandler.class, (Object)ReloadSoundCommand.mc.func_147118_V(), new String[] { "sndManager", "field_147694_f" });
            sndManager.func_148596_a();
            Command.sendMessage("§aReloaded Sound System.");
        }
        catch (Exception e) {
            System.out.println("Could not restart sound manager: " + e.toString());
            e.printStackTrace();
            Command.sendMessage("§cCouldnt Reload Sound System!");
        }
    }
}
