package com.esoterik.client.features.modules.render;

import com.esoterik.client.features.modules.*;
import com.esoterik.client.features.setting.*;
import java.util.*;
import net.minecraft.entity.item.*;
import java.util.concurrent.*;
import com.esoterik.client.event.events.*;
import com.esoterik.client.features.modules.client.*;
import com.esoterik.client.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import java.awt.*;

public class CrystalChams extends Module
{
    public Setting<Boolean> colorSync;
    public Setting<Boolean> rainbow;
    public Setting<Boolean> throughWalls;
    public Setting<Boolean> wireframe;
    public Setting<Float> lineWidth;
    public Setting<Boolean> wireframeThroughWalls;
    public Setting<Integer> speed;
    public Setting<Boolean> xqz;
    public Setting<Integer> saturation;
    public Setting<Integer> brightness;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    public Map<EntityEnderCrystal, Float> scaleMap;
    public Setting<Integer> hiddenRed;
    public Setting<Integer> hiddenGreen;
    public Setting<Integer> hiddenBlue;
    public Setting<Integer> hiddenAlpha;
    public Setting<Float> scale;
    public static CrystalChams INSTANCE;
    
    public CrystalChams() {
        super("CrystalChams", "Renders players through walls.", Category.RENDER, false, false, false);
        this.colorSync = (Setting<Boolean>)this.register(new Setting("ColorSync", (T)false));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false));
        this.throughWalls = (Setting<Boolean>)this.register(new Setting("ThroughWalls", (T)true));
        this.wireframe = (Setting<Boolean>)this.register(new Setting("Outline", (T)false));
        this.lineWidth = (Setting<Float>)this.register(new Setting("OutlineWidth", (T)1.0f, (T)0.1f, (T)3.0f));
        this.wireframeThroughWalls = (Setting<Boolean>)this.register(new Setting("OutlineThroughWalls", (T)true));
        this.speed = (Setting<Integer>)this.register(new Setting("Speed", (T)40, (T)1, (T)100, v -> this.rainbow.getValue()));
        this.xqz = (Setting<Boolean>)this.register(new Setting("XQZ", (T)false, v -> !this.rainbow.getValue() && this.throughWalls.getValue()));
        this.saturation = (Setting<Integer>)this.register(new Setting("Saturation", (T)50, (T)0, (T)100, v -> this.rainbow.getValue()));
        this.brightness = (Setting<Integer>)this.register(new Setting("Brightness", (T)100, (T)0, (T)100, v -> this.rainbow.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.scaleMap = new ConcurrentHashMap<EntityEnderCrystal, Float>();
        this.hiddenRed = (Setting<Integer>)this.register(new Setting("Hidden Red", (T)255, (T)0, (T)255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenGreen = (Setting<Integer>)this.register(new Setting("Hidden Green", (T)0, (T)0, (T)255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenBlue = (Setting<Integer>)this.register(new Setting("Hidden Blue", (T)255, (T)0, (T)255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenAlpha = (Setting<Integer>)this.register(new Setting("Hidden Alpha", (T)255, (T)0, (T)255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.scale = (Setting<Float>)this.register(new Setting("Scale", (T)1.0f, (T)0.1f, (T)2.0f));
        this.setInstance();
    }
    
    private void setInstance() {
        CrystalChams.INSTANCE = this;
    }
    
    public static CrystalChams getInstance() {
        if (CrystalChams.INSTANCE == null) {
            CrystalChams.INSTANCE = new CrystalChams();
        }
        return CrystalChams.INSTANCE;
    }
    
    public void onRenderModel(final RenderEntityModelEvent event) {
        if (event.getStage() != 0 || !(event.entity instanceof EntityEnderCrystal) || !this.wireframe.getValue()) {
            return;
        }
        final Color color = this.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), false);
        final boolean fancyGraphics = CrystalChams.mc.field_71474_y.field_74347_j;
        CrystalChams.mc.field_71474_y.field_74347_j = false;
        final float gamma = CrystalChams.mc.field_71474_y.field_74333_Y;
        CrystalChams.mc.field_71474_y.field_74333_Y = 10000.0f;
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glPolygonMode(1032, 6913);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        if (this.wireframeThroughWalls.getValue()) {
            GL11.glDisable(2929);
        }
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GlStateManager.func_179112_b(770, 771);
        GlStateManager.func_179131_c(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        GlStateManager.func_187441_d((float)this.lineWidth.getValue());
        event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    
    static {
        CrystalChams.INSTANCE = new CrystalChams();
    }
}
