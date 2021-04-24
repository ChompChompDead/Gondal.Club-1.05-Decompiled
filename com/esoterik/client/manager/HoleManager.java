package com.esoterik.client.manager;

import com.esoterik.client.features.*;
import java.util.concurrent.atomic.*;
import com.esoterik.client.features.modules.client.*;
import com.esoterik.client.features.modules.render.*;
import com.esoterik.client.features.modules.combat.*;
import java.util.concurrent.*;
import com.esoterik.client.*;
import java.util.*;
import net.minecraft.entity.player.*;
import com.esoterik.client.util.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;

public class HoleManager extends Feature implements Runnable
{
    private static final BlockPos[] surroundOffset;
    private final List<BlockPos> midSafety;
    private final Timer syncTimer;
    private final AtomicBoolean shouldInterrupt;
    private final Timer holeTimer;
    private List<BlockPos> holes;
    private ScheduledExecutorService executorService;
    private int lastUpdates;
    private Thread thread;
    
    public HoleManager() {
        this.midSafety = new ArrayList<BlockPos>();
        this.syncTimer = new Timer();
        this.shouldInterrupt = new AtomicBoolean(false);
        this.holeTimer = new Timer();
        this.holes = new ArrayList<BlockPos>();
        this.lastUpdates = 0;
    }
    
    public void update() {
        if (Managers.getInstance().holeThread.getValue() == Managers.ThreadMode.WHILE) {
            if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive() || this.syncTimer.passedMs(Managers.getInstance().holeSync.getValue())) {
                if (this.thread == null) {
                    this.thread = new Thread(this);
                }
                else if (this.syncTimer.passedMs(Managers.getInstance().holeSync.getValue()) && !this.shouldInterrupt.get()) {
                    this.shouldInterrupt.set(true);
                    this.syncTimer.reset();
                    return;
                }
                if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
                    this.thread = new Thread(this);
                }
                if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
                    try {
                        this.thread.start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.syncTimer.reset();
                }
            }
        }
        else if (Managers.getInstance().holeThread.getValue() == Managers.ThreadMode.WHILE) {
            if (this.executorService == null || this.executorService.isTerminated() || this.executorService.isShutdown() || this.syncTimer.passedMs(10000L) || this.lastUpdates != Managers.getInstance().holeUpdates.getValue()) {
                this.lastUpdates = Managers.getInstance().holeUpdates.getValue();
                if (this.executorService != null) {
                    this.executorService.shutdown();
                }
                this.executorService = this.getExecutor();
            }
        }
        else if (this.holeTimer.passedMs(Managers.getInstance().holeUpdates.getValue()) && !Feature.fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn())) {
            this.holes = this.calcHoles();
            this.holeTimer.reset();
        }
    }
    
    public void settingChanged() {
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
        if (this.thread != null) {
            this.shouldInterrupt.set(true);
        }
    }
    
    private ScheduledExecutorService getExecutor() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, Managers.getInstance().holeUpdates.getValue(), TimeUnit.MILLISECONDS);
        return service;
    }
    
    public boolean isPlayerInHole() {
        for (final BlockPos pos : esohack.holeManager.getSortedHoles()) {
            if (pos.equals((Object)new BlockPos(HoleESP.mc.field_71439_g.field_70165_t, HoleESP.mc.field_71439_g.field_70163_u, HoleESP.mc.field_71439_g.field_70161_v))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void run() {
        if (Managers.getInstance().holeThread.getValue() == Managers.ThreadMode.WHILE) {
            while (!this.shouldInterrupt.get()) {
                if (!Feature.fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn())) {
                    this.holes = this.calcHoles();
                }
                try {
                    Thread.sleep(Managers.getInstance().holeUpdates.getValue());
                }
                catch (InterruptedException e) {
                    this.thread.interrupt();
                    e.printStackTrace();
                }
            }
            this.shouldInterrupt.set(false);
            this.syncTimer.reset();
            Thread.currentThread().interrupt();
            return;
        }
        if (Managers.getInstance().holeThread.getValue() == Managers.ThreadMode.POOL && !Feature.fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn())) {
            this.holes = this.calcHoles();
        }
    }
    
    public List<BlockPos> getHoles() {
        return this.holes;
    }
    
    public List<BlockPos> getMidSafety() {
        return this.midSafety;
    }
    
    public List<BlockPos> getSortedHoles() {
        this.holes.sort(Comparator.comparingDouble(hole -> HoleManager.mc.field_71439_g.func_174818_b(hole)));
        return this.getHoles();
    }
    
    public List<BlockPos> calcHoles() {
        final ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        this.midSafety.clear();
        final List<BlockPos> positions = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)HoleManager.mc.field_71439_g), Managers.getInstance().holeRange.getValue(), (int)(Object)Managers.getInstance().holeRange.getValue(), false, true, 0);
        for (final BlockPos pos : positions) {
            if (HoleManager.mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a) && HoleManager.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
                if (!HoleManager.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
                    continue;
                }
                boolean isSafe = true;
                boolean midSafe = true;
                for (final BlockPos offset : HoleManager.surroundOffset) {
                    final Block block = HoleManager.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
                    if (BlockUtil.isBlockUnSolid(block)) {
                        midSafe = false;
                    }
                    if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z && block != Blocks.field_150477_bB) {
                        if (block != Blocks.field_150467_bQ) {
                            isSafe = false;
                        }
                    }
                }
                if (isSafe) {
                    safeSpots.add(pos);
                }
                if (!midSafe) {
                    continue;
                }
                this.midSafety.add(pos);
            }
        }
        return safeSpots;
    }
    
    public boolean isSafe(final BlockPos pos) {
        boolean isSafe = true;
        for (final BlockPos offset : HoleManager.surroundOffset) {
            final Block block = HoleManager.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
            if (block != Blocks.field_150357_h) {
                isSafe = false;
                break;
            }
        }
        return isSafe;
    }
    
    static {
        surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true, true));
    }
}
