package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {

	ArrayList<String> Modes = new ArrayList<>();
	static Minecraft mc = Minecraft.getMinecraft();

	public NoSlowdown() {
		super("NoSlowdown", 0, Category.Movement);
		Modes.add("NCP");
		Modes.add("AAC");
		Modes.add("GommeHD");
		Modes.add("Hypixel");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", Modes));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isBlocking()) {
				mc.thePlayer.sendQueue.addToSendQueue(
						new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("GommeHD")) {
			if (mc.thePlayer.isBlocking() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
				mc.thePlayer.sendQueue.addToSendQueue(
						new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isEating() && mc.thePlayer.onGround) {
				setSpeed(0.08);
			}
			if (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow
					&& mc.thePlayer.onGround) {
				setSpeed(0.08);
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCP")) {
			if (mc.thePlayer.isBlocking()) {
				mc.thePlayer.sendQueue.addToSendQueue(
						new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
			}
		}
		super.onUpdate();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

}
