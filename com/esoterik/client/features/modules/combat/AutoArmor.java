package com.esoterik.client.features.modules.combat;

import com.esoterik.client.features.modules.*;
import com.esoterik.client.features.setting.*;
import java.util.concurrent.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import com.esoterik.client.features.gui.*;
import net.minecraftforge.fml.common.eventhandler.*;
import com.esoterik.client.features.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import com.esoterik.client.*;
import net.minecraft.item.*;
import java.util.*;
import com.esoterik.client.util.*;
import net.minecraft.entity.player.*;

public class AutoArmor extends Module
{
    private final Setting<Integer> delay;
    private final Setting<Boolean> mendingTakeOff;
    private final Setting<Integer> closestEnemy;
    private final Setting<Integer> helmetThreshold;
    private final Setting<Integer> chestThreshold;
    private final Setting<Integer> legThreshold;
    private final Setting<Integer> bootsThreshold;
    private final Setting<Boolean> curse;
    private final Setting<Integer> actions;
    private final Setting<Bind> elytraBind;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> updateController;
    private final Setting<Boolean> shiftClick;
    private final Timer timer;
    private final Timer elytraTimer;
    private final Queue<InventoryUtil.Task> taskList;
    private final List<Integer> doneSlots;
    private boolean elytraOn;
    
    public AutoArmor() {
        super("AutoArmor", "Puts Armor on for you.", Category.COMBAT, true, false, false);
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)50, (T)0, (T)500));
        this.mendingTakeOff = (Setting<Boolean>)this.register(new Setting("AutoMend", (T)false));
        this.closestEnemy = (Setting<Integer>)this.register(new Setting("Enemy", (T)8, (T)1, (T)20, v -> this.mendingTakeOff.getValue()));
        this.helmetThreshold = (Setting<Integer>)this.register(new Setting("Helmet%", (T)80, (T)1, (T)100, v -> this.mendingTakeOff.getValue()));
        this.chestThreshold = (Setting<Integer>)this.register(new Setting("Chest%", (T)80, (T)1, (T)100, v -> this.mendingTakeOff.getValue()));
        this.legThreshold = (Setting<Integer>)this.register(new Setting("Legs%", (T)80, (T)1, (T)100, v -> this.mendingTakeOff.getValue()));
        this.bootsThreshold = (Setting<Integer>)this.register(new Setting("Boots%", (T)80, (T)1, (T)100, v -> this.mendingTakeOff.getValue()));
        this.curse = (Setting<Boolean>)this.register(new Setting("CurseOfBinding", (T)false));
        this.actions = (Setting<Integer>)this.register(new Setting("Actions", (T)3, (T)1, (T)12));
        this.elytraBind = (Setting<Bind>)this.register(new Setting("Elytra", (T)new Bind(-1)));
        this.tps = (Setting<Boolean>)this.register(new Setting("TpsSync", (T)true));
        this.updateController = (Setting<Boolean>)this.register(new Setting("Update", (T)true));
        this.shiftClick = (Setting<Boolean>)this.register(new Setting("ShiftClick", (T)false));
        this.timer = new Timer();
        this.elytraTimer = new Timer();
        this.taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
        this.doneSlots = new ArrayList<Integer>();
        this.elytraOn = false;
    }
    
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && !(AutoArmor.mc.field_71462_r instanceof esohackGui) && this.elytraBind.getValue().getKey() == Keyboard.getEventKey()) {
            this.elytraOn = !this.elytraOn;
        }
    }
    
    @Override
    public void onLogin() {
        this.timer.reset();
        this.elytraTimer.reset();
    }
    
    @Override
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.elytraOn = false;
    }
    
    @Override
    public void onLogout() {
        this.taskList.clear();
        this.doneSlots.clear();
    }
    
    @Override
    public void onTick() {
        if (Feature.fullNullCheck() || (AutoArmor.mc.field_71462_r instanceof GuiContainer && !(AutoArmor.mc.field_71462_r instanceof GuiInventory))) {
            return;
        }
        if (this.taskList.isEmpty()) {
            if (this.mendingTakeOff.getValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && AutoArmor.mc.field_71474_y.field_74313_G.func_151470_d() && (this.isSafe() || EntityUtil.isSafe((Entity)AutoArmor.mc.field_71439_g, 1, false, true))) {
                final ItemStack helm = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
                if (!helm.field_190928_g) {
                    final int helmDamage = DamageUtil.getRoundedDamage(helm);
                    if (helmDamage >= this.helmetThreshold.getValue()) {
                        this.takeOffSlot(5);
                    }
                }
                final ItemStack chest = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c();
                if (!chest.field_190928_g) {
                    final int chestDamage = DamageUtil.getRoundedDamage(chest);
                    if (chestDamage >= this.chestThreshold.getValue()) {
                        this.takeOffSlot(6);
                    }
                }
                final ItemStack legging = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c();
                if (!legging.field_190928_g) {
                    final int leggingDamage = DamageUtil.getRoundedDamage(legging);
                    if (leggingDamage >= this.legThreshold.getValue()) {
                        this.takeOffSlot(7);
                    }
                }
                final ItemStack feet = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c();
                if (!feet.field_190928_g) {
                    final int bootDamage = DamageUtil.getRoundedDamage(feet);
                    if (bootDamage >= this.bootsThreshold.getValue()) {
                        this.takeOffSlot(8);
                    }
                }
                return;
            }
            final ItemStack helm = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
            if (helm.func_77973_b() == Items.field_190931_a) {
                final int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, this.curse.getValue());
                if (slot != -1) {
                    this.getSlotOn(5, slot);
                }
            }
            final ItemStack chest = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c();
            if (chest.func_77973_b() == Items.field_190931_a) {
                if (this.taskList.isEmpty()) {
                    if (this.elytraOn && this.elytraTimer.passedMs(500L)) {
                        final int elytraSlot = InventoryUtil.findItemInventorySlot(Items.field_185160_cR, false);
                        if (elytraSlot != -1) {
                            if ((elytraSlot < 5 && elytraSlot > 1) || !this.shiftClick.getValue()) {
                                this.taskList.add(new InventoryUtil.Task(elytraSlot));
                                this.taskList.add(new InventoryUtil.Task(6));
                            }
                            else {
                                this.taskList.add(new InventoryUtil.Task(elytraSlot, true));
                            }
                            if (this.updateController.getValue()) {
                                this.taskList.add(new InventoryUtil.Task());
                            }
                            this.elytraTimer.reset();
                        }
                    }
                    else if (!this.elytraOn) {
                        final int slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, this.curse.getValue());
                        if (slot2 != -1) {
                            this.getSlotOn(6, slot2);
                        }
                    }
                }
            }
            else if (this.elytraOn && chest.func_77973_b() != Items.field_185160_cR && this.elytraTimer.passedMs(500L)) {
                if (this.taskList.isEmpty()) {
                    final int slot2 = InventoryUtil.findItemInventorySlot(Items.field_185160_cR, false);
                    if (slot2 != -1) {
                        this.taskList.add(new InventoryUtil.Task(slot2));
                        this.taskList.add(new InventoryUtil.Task(6));
                        this.taskList.add(new InventoryUtil.Task(slot2));
                        if (this.updateController.getValue()) {
                            this.taskList.add(new InventoryUtil.Task());
                        }
                    }
                    this.elytraTimer.reset();
                }
            }
            else if (!this.elytraOn && chest.func_77973_b() == Items.field_185160_cR && this.elytraTimer.passedMs(500L) && this.taskList.isEmpty()) {
                int slot2 = InventoryUtil.findItemInventorySlot((Item)Items.field_151163_ad, false);
                if (slot2 == -1) {
                    slot2 = InventoryUtil.findItemInventorySlot((Item)Items.field_151030_Z, false);
                    if (slot2 == -1) {
                        slot2 = InventoryUtil.findItemInventorySlot((Item)Items.field_151171_ah, false);
                        if (slot2 == -1) {
                            slot2 = InventoryUtil.findItemInventorySlot((Item)Items.field_151023_V, false);
                            if (slot2 == -1) {
                                slot2 = InventoryUtil.findItemInventorySlot((Item)Items.field_151027_R, false);
                            }
                        }
                    }
                }
                if (slot2 != -1) {
                    this.taskList.add(new InventoryUtil.Task(slot2));
                    this.taskList.add(new InventoryUtil.Task(6));
                    this.taskList.add(new InventoryUtil.Task(slot2));
                    if (this.updateController.getValue()) {
                        this.taskList.add(new InventoryUtil.Task());
                    }
                }
                this.elytraTimer.reset();
            }
            final ItemStack legging = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c();
            if (legging.func_77973_b() == Items.field_190931_a) {
                final int slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, this.curse.getValue());
                if (slot3 != -1) {
                    this.getSlotOn(7, slot3);
                }
            }
            final ItemStack feet = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c();
            if (feet.func_77973_b() == Items.field_190931_a) {
                final int slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, this.curse.getValue());
                if (slot4 != -1) {
                    this.getSlotOn(8, slot4);
                }
            }
        }
        if (this.timer.passedMs((int)(this.delay.getValue() * (this.tps.getValue() ? esohack.serverManager.getTpsFactor() : 1.0f)))) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < this.actions.getValue(); ++i) {
                    final InventoryUtil.Task task = this.taskList.poll();
                    if (task != null) {
                        task.run();
                    }
                }
            }
            this.timer.reset();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.elytraOn) {
            return "Elytra";
        }
        return null;
    }
    
    private void takeOffSlot(final int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            for (final int i : InventoryUtil.findEmptySlots(false)) {
                if (!this.doneSlots.contains(target)) {
                    target = i;
                    this.doneSlots.add(i);
                }
            }
            if (target != -1) {
                if ((target < 5 && target > 0) || !this.shiftClick.getValue()) {
                    this.taskList.add(new InventoryUtil.Task(slot));
                    this.taskList.add(new InventoryUtil.Task(target));
                }
                else {
                    this.taskList.add(new InventoryUtil.Task(slot, true));
                }
                if (this.updateController.getValue()) {
                    this.taskList.add(new InventoryUtil.Task());
                }
            }
        }
    }
    
    private void getSlotOn(final int slot, final int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object)target);
            if ((target < 5 && target > 0) || !this.shiftClick.getValue()) {
                this.taskList.add(new InventoryUtil.Task(target));
                this.taskList.add(new InventoryUtil.Task(slot));
            }
            else {
                this.taskList.add(new InventoryUtil.Task(target, true));
            }
            if (this.updateController.getValue()) {
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }
    
    private boolean isSafe() {
        final EntityPlayer closest = EntityUtil.getClosestEnemy(this.closestEnemy.getValue());
        return closest == null || AutoArmor.mc.field_71439_g.func_70068_e((Entity)closest) >= MathUtil.square(this.closestEnemy.getValue());
    }
}
