package com.esoterik.client.features.modules.misc;

import com.esoterik.client.features.modules.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import com.esoterik.client.features.command.*;
import java.util.*;

public class DonkeyNotifier extends Module
{
    private static DonkeyNotifier instance;
    private Set<Entity> entities;
    
    public DonkeyNotifier() {
        super("DonkeyNotifier", "Notifies you when a donkey is discovered", Category.MISC, true, false, false);
        this.entities = new HashSet<Entity>();
        DonkeyNotifier.instance = this;
    }
    
    @Override
    public void onEnable() {
        this.entities.clear();
    }
    
    @Override
    public void onUpdate() {
        for (final Entity entity : DonkeyNotifier.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityDonkey) {
                if (this.entities.contains(entity)) {
                    continue;
                }
                Command.sendMessage("Donkey Detected at: " + entity.field_70165_t + "x, " + entity.field_70163_u + "y, " + entity.field_70161_v + "z.");
                this.entities.add(entity);
            }
        }
    }
}
