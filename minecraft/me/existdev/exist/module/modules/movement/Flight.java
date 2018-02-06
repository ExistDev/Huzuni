package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventBlock;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public class Flight extends Module {

	ArrayList<String> Modes = new ArrayList<>();
	private double moveSpeedVanilla;

	boolean lemon = true;

	public Flight() {
		super("Flight", 0, Category.Movement);
		Modes.add("Hypixel");
		Modes.add("AirWalk");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "Hypixel", Modes));
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")) {
			Hypixel();
		}
		super.onUpdate();
	}

	@EventTarget
	public void onUpdate(EventBlock event) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AirWalk")) {
			return;
		}
		if (!mc.gameSettings.keyBindSneak.pressed && mc.thePlayer.posY > event.getPos().getY()) {
			if (isBlockValid(event.getBlock()))
				event.setAabb(new AxisAlignedBB(event.getPos(), event.getPos().add(1.0, 1.0, 1.0)));
		}
	}

	private void Hypixel() {
		mc.thePlayer.motionY = 0;
		if (mc.thePlayer.ticksExisted % 3 == 0) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY - 0.0000000001, mc.thePlayer.posZ, true));
		}
		if (mc.gameSettings.keyBindSneak.pressed) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0400000001, mc.thePlayer.posZ);
		} else if (mc.gameSettings.keyBindJump.pressed) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0400000001, mc.thePlayer.posZ);
		}
		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000000001, mc.thePlayer.posZ);
		mc.thePlayer.onGround = true;
		mc.thePlayer.setSprinting(false);
	}

	private boolean isBlockValid(Block block) {
		return block instanceof BlockAir;
	}

}
