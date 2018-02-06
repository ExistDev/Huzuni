package me.existdev.exist.module.modules.render;

import java.util.ArrayList;
import java.util.Iterator;

import com.darkmagician6.eventapi.EventManager;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;

public class ChestESP extends Module {

	ArrayList<String> Modes = new ArrayList();

	public ChestESP() {
		super("ChestESP", 0, Category.Render);
		Modes.add("CSGO");
		Modes.add("Box");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "CSGO", Modes));
	}

}
