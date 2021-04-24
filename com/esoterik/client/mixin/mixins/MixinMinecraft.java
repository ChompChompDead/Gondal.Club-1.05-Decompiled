package com.esoterik.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.*;
import com.esoterik.client.features.modules.client.*;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.crash.*;
import net.minecraft.client.multiplayer.*;
import com.esoterik.client.features.modules.render.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.esoterik.client.*;

@Mixin({ Minecraft.class })
public abstract class MixinMinecraft
{
    @Inject(method = { "Lnet/minecraft/client/Minecraft;getLimitFramerate()I" }, at = { @At("HEAD") }, cancellable = true)
    public void getLimitFramerateHook(final CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        try {
            if (Managers.getInstance().unfocusedCpu.getValue() && !Display.isActive()) {
                callbackInfoReturnable.setReturnValue(Managers.getInstance().cpuFPS.getValue());
            }
        }
        catch (NullPointerException ex) {}
    }
    
    @Redirect(method = { "runGameLoop" }, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;sync(I)V"))
    public void syncHook(final int maxFps) {
        if (Managers.getInstance().betterFrames.getValue()) {
            Display.sync((int)Managers.getInstance().betterFPS.getValue());
        }
        else {
            Display.sync(maxFps);
        }
    }
    
    @Redirect(method = { "run" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReportHook(final Minecraft minecraft, final CrashReport crashReport) {
        this.unload();
    }
    
    @Redirect(method = { "runTick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"))
    public void doVoidFogParticlesHook(final WorldClient world, final int x, final int y, final int z) {
        NoRender.getInstance().doVoidFogParticles(x, y, z);
    }
    
    @Inject(method = { "shutdown" }, at = { @At("HEAD") })
    public void shutdownHook(final CallbackInfo info) {
        this.unload();
    }
    
    private void unload() {
        System.out.println("Shutting down: saving configuration");
        esohack.onUnload();
        System.out.println("Configuration saved.");
    }
}
