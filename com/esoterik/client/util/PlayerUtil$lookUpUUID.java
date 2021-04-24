package com.esoterik.client.util;

import net.minecraft.client.network.*;
import java.util.*;
import java.util.function.*;
import com.esoterik.client.features.command.*;
import com.mojang.util.*;
import com.google.gson.*;

public static class lookUpUUID implements Runnable
{
    private volatile UUID uuid;
    private final String name;
    
    public lookUpUUID(final String name) {
        this.name = name;
    }
    
    @Override
    public void run() {
        NetworkPlayerInfo profile;
        try {
            final ArrayList<NetworkPlayerInfo> infoMap = new ArrayList<NetworkPlayerInfo>(Objects.requireNonNull(Util.mc.func_147114_u()).func_175106_d());
            profile = infoMap.stream().filter(this::lambda$run$0).findFirst().orElse(null);
            assert profile != null;
            this.uuid = profile.func_178845_a().getId();
        }
        catch (Exception e2) {
            profile = null;
        }
        if (profile == null) {
            Command.sendMessage("Player isn't online. Looking up UUID..");
            final String s = PlayerUtil.requestIDs("[\"" + this.name + "\"]");
            if (s == null || s.isEmpty()) {
                Command.sendMessage("Couldn't find player ID. Are you connected to the internet? (0)");
            }
            else {
                final JsonElement element = new JsonParser().parse(s);
                if (element.getAsJsonArray().size() == 0) {
                    Command.sendMessage("Couldn't find player ID. (1)");
                }
                else {
                    try {
                        final String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                        this.uuid = UUIDTypeAdapter.fromString(id);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Command.sendMessage("Couldn't find player ID. (2)");
                    }
                }
            }
        }
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public String getName() {
        return this.name;
    }
    
    static {
        lookUpUUID.$assertionsDisabled = !PlayerUtil.class.desiredAssertionStatus();
    }
}
