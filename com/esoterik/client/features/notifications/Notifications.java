package com.esoterik.client.features.notifications;

import com.esoterik.client.features.modules.client.*;
import com.esoterik.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import com.esoterik.client.util.*;

public class Notifications
{
    private final String text;
    private final long disableTime;
    private final float width;
    private final Timer timer;
    
    public Notifications(final String text, final long disableTime) {
        this.timer = new Timer();
        this.text = text;
        this.disableTime = disableTime;
        this.width = esohack.moduleManager.getModuleByClass(HUD.class).renderer.getStringWidth(text);
        this.timer.reset();
    }
    
    public void onDraw(final int y) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        if (this.timer.passedMs(this.disableTime)) {
            esohack.notificationManager.getNotifications().remove(this);
        }
        RenderUtil.drawRect(scaledResolution.func_78326_a() - 4 - this.width, y, scaledResolution.func_78326_a() - 2, y + esohack.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 3, 1962934272);
        esohack.moduleManager.getModuleByClass(HUD.class).renderer.drawString(this.text, scaledResolution.func_78326_a() - this.width - 3.0f, y + 2, -1, true);
    }
}
