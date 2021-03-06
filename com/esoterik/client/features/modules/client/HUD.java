package com.esoterik.client.features.modules.client;

import com.esoterik.client.features.modules.*;
import com.esoterik.client.features.setting.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import com.esoterik.client.event.events.*;
import com.esoterik.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.potion.*;
import java.text.*;
import net.minecraft.client.*;
import java.util.*;
import net.minecraft.init.*;
import java.util.function.*;
import net.minecraft.client.renderer.*;
import com.esoterik.client.util.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class HUD extends Module
{
    public Setting<Boolean> colorSync;
    public Setting<Boolean> rainbow;
    public Setting<Integer> rainbowSpeed;
    public Setting<Boolean> potionIcons;
    private final Setting<Boolean> watermark;
    private final Setting<Boolean> arrayList;
    private final Setting<Boolean> serverBrand;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> fps;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> direction;
    private final Setting<Boolean> speed;
    private final Setting<Boolean> potions;
    public Setting<Boolean> textRadar;
    private final Setting<Boolean> armor;
    private final Setting<Boolean> percent;
    private final Setting<Boolean> totems;
    private final Setting<Greeter> greeter;
    public Setting<Boolean> time;
    public Setting<Integer> hudRed;
    public Setting<Integer> hudGreen;
    public Setting<Integer> hudBlue;
    private static HUD INSTANCE;
    private Map<String, Integer> players;
    private static final ResourceLocation box;
    private static final ItemStack totem;
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;
    private final Timer timer;
    private boolean shadow;
    
    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Category.CLIENT, true, false, false);
        this.colorSync = (Setting<Boolean>)this.register(new Setting("ColorSync", (T)false));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false, v -> !this.colorSync.getValue()));
        this.rainbowSpeed = (Setting<Integer>)this.register(new Setting("Speed", (T)70, (T)0, (T)400, v -> this.rainbow.getValue() && !this.colorSync.getValue()));
        this.potionIcons = (Setting<Boolean>)this.register(new Setting("RemovePotionIcons", (T)true, "Draws Potion Icons."));
        this.watermark = (Setting<Boolean>)this.register(new Setting("Watermark", (T)false, "WaterMark"));
        this.arrayList = (Setting<Boolean>)this.register(new Setting("ArrayList", (T)false, "Lists the active modules."));
        this.serverBrand = (Setting<Boolean>)this.register(new Setting("ServerBrand", (T)false, "Brand of the server you are on."));
        this.ping = (Setting<Boolean>)this.register(new Setting("Ping", (T)false, "Your response time to the server."));
        this.tps = (Setting<Boolean>)this.register(new Setting("TPS", (T)false, "Ticks per second of the server."));
        this.fps = (Setting<Boolean>)this.register(new Setting("FPS", (T)false, "Your frames per second."));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", (T)false, "Your current coordinates"));
        this.direction = (Setting<Boolean>)this.register(new Setting("Direction", (T)false, "The Direction you are facing."));
        this.speed = (Setting<Boolean>)this.register(new Setting("Speed", (T)false, "Your Speed"));
        this.potions = (Setting<Boolean>)this.register(new Setting("Potions", (T)false, "Your Speed"));
        this.textRadar = (Setting<Boolean>)this.register(new Setting("TextRadar", (T)false, "A TextRadar"));
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", (T)false, "ArmorHUD"));
        this.percent = (Setting<Boolean>)this.register(new Setting("Percent", (T)false, v -> this.armor.getValue()));
        this.totems = (Setting<Boolean>)this.register(new Setting("Totems", (T)false, "TotemHUD"));
        this.greeter = (Setting<Greeter>)this.register(new Setting("Greeter", (T)Greeter.NONE, "Greets you."));
        this.time = (Setting<Boolean>)this.register(new Setting("Time", (T)false, "The time"));
        this.hudRed = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.hudGreen = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.hudBlue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.players = new HashMap<String, Integer>();
        this.timer = new Timer();
        this.shadow = true;
        this.setInstance();
    }
    
    private void setInstance() {
        HUD.INSTANCE = this;
    }
    
    public static HUD getInstance() {
        if (HUD.INSTANCE == null) {
            HUD.INSTANCE = new HUD();
        }
        return HUD.INSTANCE;
    }
    
    @Override
    public void onUpdate() {
        if (this.timer.passedMs(Managers.getInstance().textRadarUpdates.getValue())) {
            this.players = this.getTextRadarPlayers();
            this.timer.reset();
        }
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        this.color = (this.colorSync.getValue() ? ColorUtil.toARGB(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), 255) : ColorUtil.toRGBA(this.hudRed.getValue(), this.hudGreen.getValue(), this.hudBlue.getValue()));
        final String whiteString = "?f";
        if (this.watermark.getValue()) {
            final int[] arrayOfInt = { 1 };
            final String string = esohack.getName() + " v" + "1.0.5";
            final char[] stringToCharArray = string.toCharArray();
            float f = 0.0f;
            for (final char c : stringToCharArray) {
                this.renderer.drawString(String.valueOf(c), 2.0f + f, 2.0f, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += this.renderer.getStringWidth(String.valueOf(c));
                ++arrayOfInt[0];
            }
        }
        int j = 0;
        if (this.arrayList.getValue()) {
            final int[] arrayOfInt = { 1 };
            final float f = 0.0f;
            for (int i = 0; i < esohack.moduleManager.sortedModules.size(); ++i) {
                final Module module = esohack.moduleManager.sortedModules.get(i);
                final String text = module.getDisplayName() + ((module.getDisplayInfo() != null) ? (" [?f" + module.getDisplayInfo() + "?r" + "]") : "");
                this.renderer.drawString(text, width - 2 - this.renderer.getStringWidth(text), 2 + j * 10, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                ++arrayOfInt[0];
                ++j;
            }
        }
        int i = (HUD.mc.field_71462_r instanceof GuiChat) ? 14 : 0;
        if (this.serverBrand.getValue()) {
            final String text2 = "Server brand " + esohack.serverManager.getServerBrand();
            final int[] arrayOfInt = { 1 };
            final char[] stringToCharArray = text2.toCharArray();
            float f = 0.0f;
            i += 10;
            for (final char c2 : stringToCharArray) {
                this.renderer.drawString(String.valueOf(c2), width - this.renderer.getStringWidth(text2) + f - 2.0f, height - i, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += this.renderer.getStringWidth(String.valueOf(c2));
                ++arrayOfInt[0];
            }
        }
        if (this.potions.getValue()) {
            final List<String> effects = new ArrayList<String>();
            for (final PotionEffect effect : esohack.potionManager.getOwnPotions()) {
                final String text2 = esohack.potionManager.getPotionString(effect);
                effects.add(text2);
            }
            Collections.sort(effects, Comparator.comparing((Function<? super String, ? extends Comparable>)String::length));
            for (int x = effects.size() - 1; x >= 0; --x) {
                i += 10;
                final String text2 = effects.get(x);
                final int[] arrayOfInt = { 1 };
                float f = 0.0f;
                final char[] charArray;
                final char[] stringToCharArray = charArray = text2.toCharArray();
                for (final char c3 : charArray) {
                    this.renderer.drawString(String.valueOf(c3), width - this.renderer.getStringWidth(text2) + f - 2.0f, height - i, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                    f += this.renderer.getStringWidth(String.valueOf(c3));
                    ++arrayOfInt[0];
                }
            }
        }
        if (this.speed.getValue()) {
            final String text2 = "Speed " + esohack.speedManager.getSpeedKpH() + " km/h";
            final int[] arrayOfInt = { 1 };
            final char[] stringToCharArray = text2.toCharArray();
            float f = 0.0f;
            i += 10;
            for (final char c2 : stringToCharArray) {
                this.renderer.drawString(String.valueOf(c2), width - this.renderer.getStringWidth(text2) + f - 2.0f, height - i, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += this.renderer.getStringWidth(String.valueOf(c2));
                ++arrayOfInt[0];
            }
        }
        if (this.time.getValue()) {
            final String text2 = "Time " + new SimpleDateFormat("h:mm a").format(new Date());
            final int[] arrayOfInt = { 1 };
            final char[] stringToCharArray = text2.toCharArray();
            float f = 0.0f;
            i += 10;
            for (final char c2 : stringToCharArray) {
                this.renderer.drawString(String.valueOf(c2), width - this.renderer.getStringWidth(text2) + f - 2.0f, height - i, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += this.renderer.getStringWidth(String.valueOf(c2));
                ++arrayOfInt[0];
            }
        }
        if (this.tps.getValue()) {
            final String text2 = "TPS " + esohack.serverManager.getTPS();
            final int[] arrayOfInt = { 1 };
            final char[] stringToCharArray = text2.toCharArray();
            float f = 0.0f;
            i += 10;
            for (final char c2 : stringToCharArray) {
                this.renderer.drawString(String.valueOf(c2), width - this.renderer.getStringWidth(text2) + f - 2.0f, height - i, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += this.renderer.getStringWidth(String.valueOf(c2));
                ++arrayOfInt[0];
            }
        }
        final String fpsText = "FPS " + Minecraft.field_71470_ab;
        String text = "Ping " + esohack.serverManager.getPing();
        if (this.fps.getValue()) {
            final int[] arrayOfInt = { 1 };
            final char[] stringToCharArray = fpsText.toCharArray();
            float f = 0.0f;
            i += 10;
            for (final char c4 : stringToCharArray) {
                this.renderer.drawString(String.valueOf(c4), width - this.renderer.getStringWidth(fpsText) + f - 2.0f, height - i, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += this.renderer.getStringWidth(String.valueOf(c4));
                ++arrayOfInt[0];
            }
        }
        if (this.ping.getValue()) {
            final int[] arrayOfInt = { 1 };
            final char[] stringToCharArray = text.toCharArray();
            float f = 0.0f;
            i += 10;
            for (final char c4 : stringToCharArray) {
                this.renderer.drawString(String.valueOf(c4), width - this.renderer.getStringWidth(text) + f - 2.0f, height - i, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += this.renderer.getStringWidth(String.valueOf(c4));
                ++arrayOfInt[0];
            }
        }
        final boolean inHell = HUD.mc.field_71441_e.func_180494_b(HUD.mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
        final int posX = (int)HUD.mc.field_71439_g.field_70165_t;
        final int posY = (int)HUD.mc.field_71439_g.field_70163_u;
        final int posZ = (int)HUD.mc.field_71439_g.field_70161_v;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(HUD.mc.field_71439_g.field_70165_t * nether);
        final int hposZ = (int)(HUD.mc.field_71439_g.field_70161_v * nether);
        esohack.notificationManager.handleNotifications(height - (i + 16));
        i = ((HUD.mc.field_71462_r instanceof GuiChat) ? 14 : 0);
        final String coordinates = posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]";
        text = (this.direction.getValue() ? (esohack.rotationManager.getDirection4D(false) + " ") : "") + (this.coords.getValue() ? coordinates : "") + "";
        final int[] arrayOfInt = { 1 };
        final char[] stringToCharArray = text.toCharArray();
        float f = 0.0f;
        i += 10;
        for (final char c5 : stringToCharArray) {
            this.renderer.drawString(String.valueOf(c5), 2.0f + f, height - i, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
            f += this.renderer.getStringWidth(String.valueOf(c5));
            ++arrayOfInt[0];
        }
        if (this.armor.getValue()) {
            this.renderArmorHUD(this.percent.getValue());
        }
        if (this.totems.getValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue() != Greeter.NONE) {
            this.renderGreeter();
        }
    }
    
    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }
    
    public void renderGreeter() {
        final int width = this.renderer.scaledWidth;
        String text = "";
        switch (this.greeter.getValue()) {
            case TIME: {
                text = text + MathUtil.getTimeOfDay() + HUD.mc.field_71439_g.getDisplayNameString();
                break;
            }
            case LONG: {
                text = text + "looking swag today, " + HUD.mc.field_71439_g.getDisplayNameString() + " :^)";
                break;
            }
            default: {
                text = text + "Welcome " + HUD.mc.field_71439_g.getDisplayNameString();
                break;
            }
        }
        final int[] arrayOfInt = { 1 };
        final char[] stringToCharArray = text.toCharArray();
        float f = 0.0f;
        for (final char c : stringToCharArray) {
            this.renderer.drawString(String.valueOf(c), width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f + f, 2.0f, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(arrayOfInt[0] * getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
            f += this.renderer.getStringWidth(String.valueOf(c));
            ++arrayOfInt[0];
        }
    }
    
    public void renderTotemHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int totems = HUD.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (HUD.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            totems += HUD.mc.field_71439_g.func_184592_cb().func_190916_E();
        }
        if (totems > 0) {
            GlStateManager.func_179098_w();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(HUD.totem, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, HUD.totem, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            this.renderer.drawStringWithShadow(totems + "", x + 19 - 2 - this.renderer.getStringWidth(totems + ""), y + 9, 16777215);
            GlStateManager.func_179126_j();
            GlStateManager.func_179140_f();
        }
    }
    
    public void renderArmorHUD(final boolean percent) {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        GlStateManager.func_179098_w();
        final int i = width / 2;
        int iteration = 0;
        final int y = height - 55 - ((HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f()) ? 10 : 0);
        for (final ItemStack is : HUD.mc.field_71439_g.field_71071_by.field_70460_b) {
            ++iteration;
            if (is.func_190926_b()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(is, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, is, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            final String s = (is.func_190916_E() > 1) ? (is.func_190916_E() + "") : "";
            this.renderer.drawStringWithShadow(s, x + 19 - 2 - this.renderer.getStringWidth(s), y + 9, 16777215);
            if (!percent) {
                continue;
            }
            int dmg = 0;
            final int itemDurability = is.func_77958_k() - is.func_77952_i();
            final float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
            final float red = 1.0f - green;
            if (percent) {
                dmg = 100 - (int)(red * 100.0f);
            }
            else {
                dmg = itemDurability;
            }
            this.renderer.drawStringWithShadow(dmg + "", x + 8 - this.renderer.getStringWidth(dmg + "") / 2, y - 11, ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.func_179126_j();
        GlStateManager.func_179140_f();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final AttackEntityEvent event) {
        this.shouldIncrement = true;
    }
    
    static {
        HUD.INSTANCE = new HUD();
        box = new ResourceLocation("textures/gui/container/shulker_box.png");
        totem = new ItemStack(Items.field_190929_cY);
    }
    
    public enum Greeter
    {
        NONE, 
        TIME, 
        LONG;
    }
}
