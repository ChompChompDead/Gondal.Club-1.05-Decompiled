package com.esoterik.client.features.gui.components.items.buttons;

import com.esoterik.client.features.setting.*;
import com.esoterik.client.*;
import com.esoterik.client.features.modules.client.*;
import com.esoterik.client.util.*;
import com.esoterik.client.features.gui.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import net.minecraft.util.*;

public class StringButton extends Button
{
    private Setting setting;
    public boolean isListening;
    private CurrentString currentString;
    
    public StringButton(final Setting setting) {
        super(setting.getName());
        this.currentString = new CurrentString("");
        this.setting = setting;
        this.width = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? esohack.colorManager.getColorWithAlpha(esohack.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue()) : esohack.colorManager.getColorWithAlpha(esohack.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue())) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
        if (this.isListening) {
            esohack.textManager.drawStringWithShadow(this.currentString.getString() + esohack.textManager.getIdleSign(), this.x + 2.3f, this.y - 1.7f - esohackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
        else {
            esohack.textManager.drawStringWithShadow((this.setting.getName().equals("Buttons") ? "Buttons " : (this.setting.getName().equals("Prefix") ? "Prefix  ?7" : "")) + this.setting.getValue(), this.x + 2.3f, this.y - 1.7f - esohackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            StringButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
        }
    }
    
    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (this.isListening) {
            switch (keyCode) {
                case 1: {
                    break;
                }
                case 28: {
                    this.enterString();
                    break;
                }
                case 14: {
                    this.setString(removeLastChar(this.currentString.getString()));
                    break;
                }
                default: {
                    if (ChatAllowedCharacters.func_71566_a(typedChar)) {
                        this.setString(this.currentString.getString() + typedChar);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        }
        else {
            this.setting.setValue(this.currentString.getString());
        }
        this.setString("");
        super.onMouseClick();
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }
    
    @Override
    public boolean getState() {
        return !this.isListening;
    }
    
    public void setString(final String newString) {
        this.currentString = new CurrentString(newString);
    }
    
    public static String removeLastChar(final String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }
    
    public static class CurrentString
    {
        private String string;
        
        public CurrentString(final String string) {
            this.string = string;
        }
        
        public String getString() {
            return this.string;
        }
    }
}
