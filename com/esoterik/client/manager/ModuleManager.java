package com.esoterik.client.manager;

import com.esoterik.client.features.*;
import com.esoterik.client.features.modules.*;
import com.esoterik.client.features.modules.combat.*;
import com.esoterik.client.features.modules.movement.*;
import com.esoterik.client.features.modules.player.*;
import com.esoterik.client.features.modules.render.*;
import com.esoterik.client.features.modules.client.*;
import com.esoterik.client.features.modules.misc.*;
import net.minecraftforge.common.*;
import java.util.function.*;
import com.esoterik.client.event.events.*;
import java.util.*;
import java.util.stream.*;
import org.lwjgl.input.*;
import com.esoterik.client.features.gui.*;

public class ModuleManager extends Feature
{
    public static ArrayList<Module> modules;
    public List<Module> sortedModules;
    boolean hasRun;
    
    public ModuleManager() {
        this.sortedModules = new ArrayList<Module>();
        this.hasRun = false;
    }
    
    public void init() {
        ModuleManager.modules.add(new AutoTrap());
        ModuleManager.modules.add(new Offhand());
        ModuleManager.modules.add(new Criticals());
        ModuleManager.modules.add(new AutoArmor());
        ModuleManager.modules.add(new Surround());
        ModuleManager.modules.add(new HoleFiller());
        ModuleManager.modules.add(new AutoGondal());
        ModuleManager.modules.add(new ChatModifier());
        ModuleManager.modules.add(new Spammer());
        ModuleManager.modules.add(new DonkeyNotifier());
        ModuleManager.modules.add(new Velocity());
        ModuleManager.modules.add(new Step());
        ModuleManager.modules.add(new ReverseStep());
        ModuleManager.modules.add(new Sprint());
        ModuleManager.modules.add(new NoSlowDown());
        ModuleManager.modules.add(new Speed());
        ModuleManager.modules.add(new FakePlayer());
        ModuleManager.modules.add(new Speedmine());
        ModuleManager.modules.add(new Replenish());
        ModuleManager.modules.add(new MCP());
        ModuleManager.modules.add(new FastPlace());
        ModuleManager.modules.add(new Burrow());
        ModuleManager.modules.add(new NoRender());
        ModuleManager.modules.add(new Fullbright());
        ModuleManager.modules.add(new Nametags());
        ModuleManager.modules.add(new CameraClip());
        ModuleManager.modules.add(new ViewModel());
        ModuleManager.modules.add(new ESP());
        ModuleManager.modules.add(new HoleESP());
        ModuleManager.modules.add(new CrystalChams());
        ModuleManager.modules.add(new Gondal());
        ModuleManager.modules.add(new Colors());
        ModuleManager.modules.add(new Notifications());
        ModuleManager.modules.add(new HUD());
        ModuleManager.modules.add(new ClickGui());
        ModuleManager.modules.add(new Managers());
        ModuleManager.modules.add(new Components());
        ModuleManager.modules.add(new RPC());
    }
    
    public Module getModuleByName(final String name) {
        for (final Module module : ModuleManager.modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
    
    public <T extends Module> T getModuleByClass(final Class<T> clazz) {
        for (final Module module : ModuleManager.modules) {
            if (clazz.isInstance(module)) {
                return (T)module;
            }
        }
        return null;
    }
    
    public void enableModule(final Class clazz) {
        final Module module = this.getModuleByClass((Class<Module>)clazz);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final Class clazz) {
        final Module module = this.getModuleByClass((Class<Module>)clazz);
        if (module != null) {
            module.disable();
        }
    }
    
    public void enableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }
    
    public boolean isModuleEnabled(final Class clazz) {
        final Module module = this.getModuleByClass((Class<Module>)clazz);
        return module != null && module.isOn();
    }
    
    public Module getModuleByDisplayName(final String displayName) {
        for (final Module module : ModuleManager.modules) {
            if (module.getDisplayName().equalsIgnoreCase(displayName)) {
                return module;
            }
        }
        return null;
    }
    
    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (final Module module : ModuleManager.modules) {
            if (module.isEnabled()) {
                enabledModules.add(module);
            }
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesByCategory(final Module.Category category) {
        final ArrayList<Module> modulesCategory = new ArrayList<Module>();
        final ArrayList<Module> list;
        ModuleManager.modules.forEach(module -> {
            if (module.getCategory() == category) {
                list.add(module);
            }
            return;
        });
        return modulesCategory;
    }
    
    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }
    
    public void onLoad() {
        ModuleManager.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
        ModuleManager.modules.forEach(Module::onLoad);
    }
    
    public void onUpdate() {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }
    
    public void onTick() {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    
    public void onRender2D(final Render2DEvent event) {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }
    
    public void onRender3D(final Render3DEvent event) {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }
    
    public void sortModules(final boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public void onLogout() {
        ModuleManager.modules.forEach(Module::onLogout);
    }
    
    public void onLogin() {
        ModuleManager.modules.forEach(Module::onLogin);
    }
    
    public void onUnload() {
        ModuleManager.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        ModuleManager.modules.forEach(Module::onUnload);
    }
    
    public void onUnloadPost() {
        for (final Module module : ModuleManager.modules) {
            module.enabled.setValue(false);
        }
    }
    
    public void onKeyPressed(final int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.field_71462_r instanceof esohackGui) {
            return;
        }
        ModuleManager.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
    
    public static void onServerUpdate() {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(Module::onServerUpdate);
    }
    
    static {
        ModuleManager.modules = new ArrayList<Module>();
    }
}
