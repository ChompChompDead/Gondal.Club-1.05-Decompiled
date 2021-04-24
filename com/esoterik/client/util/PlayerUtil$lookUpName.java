package com.esoterik.client.util;

import java.util.*;
import java.net.*;
import org.apache.commons.io.*;
import org.json.simple.*;
import java.io.*;
import org.json.simple.parser.*;
import net.minecraft.entity.player.*;

public static class lookUpName implements Runnable
{
    private volatile String name;
    private final String uuid;
    private final UUID uuidID;
    
    public lookUpName(final String input) {
        this.uuid = input;
        this.uuidID = UUID.fromString(input);
    }
    
    public lookUpName(final UUID input) {
        this.uuidID = input;
        this.uuid = input.toString();
    }
    
    @Override
    public void run() {
        this.name = this.lookUpName();
    }
    
    public String lookUpName() {
        EntityPlayer player = null;
        if (Util.mc.field_71441_e != null) {
            player = Util.mc.field_71441_e.func_152378_a(this.uuidID);
        }
        if (player == null) {
            final String url = "https://api.mojang.com/user/profiles/" + this.uuid.replace("-", "") + "/names";
            try {
                final String nameJson = IOUtils.toString(new URL(url));
                final JSONArray nameValue = (JSONArray)JSONValue.parseWithException(nameJson);
                final String playerSlot = nameValue.get(nameValue.size() - 1).toString();
                final JSONObject nameObject = (JSONObject)JSONValue.parseWithException(playerSlot);
                return nameObject.get((Object)"name").toString();
            }
            catch (IOException | ParseException ex2) {
                final Exception ex;
                final Exception e = ex;
                e.printStackTrace();
                return null;
            }
        }
        return player.func_70005_c_();
    }
    
    public String getName() {
        return this.name;
    }
}
